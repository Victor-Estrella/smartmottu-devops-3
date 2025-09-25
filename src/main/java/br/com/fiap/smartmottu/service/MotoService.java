package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.MotoRequestDto;
import br.com.fiap.smartmottu.dto.MotoResponseDto;
import br.com.fiap.smartmottu.entity.Moto;
import br.com.fiap.smartmottu.entity.StatusMoto;
import br.com.fiap.smartmottu.entity.TipoMoto;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.MotoRepository;
import br.com.fiap.smartmottu.repository.StatusMotoRepository;
import br.com.fiap.smartmottu.repository.TipoMotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    @Autowired private MotoRepository motoRepository;
    @Autowired private StatusMotoRepository statusMotoRepository;
    @Autowired private TipoMotoRepository tipoMotoRepository;

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

    public void delete(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(NotFoundException.forMoto(id));
        motoRepository.delete(moto);
    }

    public MotoResponseDto save(@Valid MotoRequestDto dto) {
        Moto moto = new Moto();
        moto.setNmChassi(dto.getNmChassi());
        moto.setPlaca(dto.getPlaca());
        moto.setUnidade(dto.getUnidade());

        StatusMoto status = statusMotoRepository.findById(dto.getStatusId())
                .orElseThrow(NotFoundException.forStatusMoto(dto.getStatusId()));
        moto.setStatus(status);

        TipoMoto tipo = tipoMotoRepository.findById(dto.getModeloId())
                .orElseThrow(NotFoundException.forTipoMoto(dto.getModeloId()));
        moto.setModelo(tipo);

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
            StatusMoto status = statusMotoRepository.findById(dto.getStatusId())
                    .orElseThrow(NotFoundException.forStatusMoto(dto.getStatusId()));
            existing.setStatus(status);
        }

        if (dto.getModeloId() != null) {
            TipoMoto tipo = tipoMotoRepository.findById(dto.getModeloId())
                    .orElseThrow(NotFoundException.forTipoMoto(dto.getModeloId()));
            existing.setModelo(tipo);
        }

        Moto saved = motoRepository.save(existing);
        return toResponse(saved);
    }

    private MotoResponseDto toResponse(Moto moto) {
        MotoResponseDto dto = new MotoResponseDto();
        dto.setIdMoto(moto.getIdMoto());
        dto.setNmChassi(moto.getNmChassi());
        dto.setPlaca(moto.getPlaca());
        dto.setUnidade(moto.getUnidade());
        dto.setStatusId(moto.getStatus() != null ? moto.getStatus().getIdStatus() : null);
        dto.setModeloId(moto.getModelo() != null ? moto.getModelo().getIdTipo() : null);
    // Nomes legíveis
    dto.setStatusName(moto.getStatus() != null && moto.getStatus().getStatus() != null
        ? moto.getStatus().getStatus().getDescricao() : null);
    dto.setModeloName(moto.getModelo() != null && moto.getModelo().getNmTipo() != null
        ? moto.getModelo().getNmTipo().getDescricao() : null);
        return dto;
    }
}
