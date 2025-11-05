package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.MotoRequestDto;
import br.com.fiap.smartmottu.dto.MotoResponseDto;
import br.com.fiap.smartmottu.entity.Aluguel;
import br.com.fiap.smartmottu.entity.Moto;
import br.com.fiap.smartmottu.entity.StatusMoto;
import br.com.fiap.smartmottu.entity.TipoMoto;
import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MotoService {

    @Autowired private MotoRepository motoRepository;
    @Autowired private TipoMotoRepository tipoMotoRepository;
    @Autowired private StatusMotoRepository statusMotoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AluguelRepository aluguelRepository;
    @Autowired
    private AluguelService aluguelService;

    public List<MotoResponseDto> getAll() {
        return motoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MotoResponseDto getById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(NotFoundException.forMoto(id));
        return toResponse(moto);
    }

    public void delete(Long motoId) {
        if (motoAtivaNoAluguel(motoId)) {
            throw new RuntimeException("Não é possível excluir a moto: ela possui um aluguel ATIVO.");
        }
    }

    public List<MotoResponseDto> getByUsuarioId(Long usuarioId) {
        return motoRepository.findByUsuario_IdUsuario(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MotoResponseDto save(@Valid MotoRequestDto dto) {
        Moto moto = new Moto();
        moto.setNmChassi(dto.getNmChassi());
        moto.setPlaca(dto.getPlaca());
        moto.setUnidade(dto.getUnidade());

        if (dto.getStatusId() != null) {
            StatusMoto status = statusMotoRepository.findByStatus(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status não encontrado: " + dto.getStatusId()));
            moto.setStatus(status);
        }

        if (dto.getModeloId() != null) {
            TipoMoto tipo = tipoMotoRepository.findByNmTipo(dto.getModeloId())
                    .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + dto.getModeloId()));
            moto.setModelo(tipo);
        }

        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        var usuario = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado: " + emailLogado));

        moto.setUsuario(usuario);

        Moto saved = motoRepository.save(moto);
        return toResponse(saved);
    }

    public MotoResponseDto update(Long id, @Valid MotoRequestDto dto) {
        Moto existing = motoRepository.findById(id)
                .orElseThrow(NotFoundException.forMoto(id));

        existing.setNmChassi(dto.getNmChassi());
        existing.setPlaca(dto.getPlaca());
        existing.setUnidade(dto.getUnidade());

        if (dto.getStatusId() != null) {
            StatusMoto status = statusMotoRepository.findByStatus(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status não encontrado: " + dto.getStatusId()));
            existing.setStatus(status);
        }

        if (dto.getModeloId() != null) {
            TipoMoto tipo = tipoMotoRepository.findByNmTipo(dto.getModeloId())
                    .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + dto.getModeloId()));
            existing.setModelo(tipo);
        }

        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        var usuario = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado: " + emailLogado));

        existing.setUsuario(usuario);

        Moto saved = motoRepository.save(existing);
        return toResponse(saved);
    }


    private MotoResponseDto toResponse(Moto moto) {
        MotoResponseDto dto = new MotoResponseDto();
        dto.setIdMoto(moto.getIdMoto());
        dto.setNmChassi(moto.getNmChassi());
        dto.setPlaca(moto.getPlaca());
        dto.setUnidade(moto.getUnidade());
        dto.setStatusId(moto.getStatus() != null ? moto.getStatus().getStatus() : null);
        dto.setModeloId(moto.getModelo() != null ? moto.getModelo().getNmTipo() : null);

        return dto;
    }

    public boolean motoAtivaNoAluguel(Long motoId) {
        List<Aluguel> alugueisDaMoto = aluguelRepository.findByMoto_IdMoto(motoId);

        for (Aluguel aluguel : alugueisDaMoto) {

            LocalDate dataInicio = aluguel.getDataInicio();
            LocalDate dataFim = aluguel.getDataFim();

            StatusAluguel status = aluguelService.calculateStatus(dataInicio, dataFim);

            if ("ATIVO".equals(status)) {
                return true;
            }
        }
        return false;
    }
}


