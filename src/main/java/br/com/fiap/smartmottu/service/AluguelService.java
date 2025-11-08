package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.AluguelRequestDto;
import br.com.fiap.smartmottu.dto.AluguelResponseDto;
import br.com.fiap.smartmottu.entity.Aluguel;
import br.com.fiap.smartmottu.entity.Moto;
import br.com.fiap.smartmottu.entity.StatusMoto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.AluguelRepository;
import br.com.fiap.smartmottu.repository.MotoRepository;
import br.com.fiap.smartmottu.repository.StatusMotoRepository;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private StatusMotoRepository statusMotoRepository;


    public AluguelResponseDto alugarMoto(AluguelRequestDto filter) {

        Usuario usuario = validarUsuario(filter.getEmail());

        boolean jaTemMoto = aluguelRepository.existsByUsuario(usuario);
        if (jaTemMoto) {
            throw new IllegalArgumentException("O usuário já possui uma moto cadastrada.");
        }

        Moto moto = validarMoto(filter.getMotoId(), usuario.getIdUsuario());

        LocalDate dataInicio = filter.getDataInicio();
        LocalDate dataFim = filter.getDataFim();

        Double valorTotal = calcularValorTotal(dataInicio, dataFim);

        atualizarStatusAlugada(moto);

        StatusAluguel statusInicial = calculateStatus(dataInicio, dataFim);

        Aluguel aluguel = Aluguel.builder()
                .usuario(usuario)
                .moto(moto)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .valorTotal(valorTotal)
                .statusAluguel(statusInicial)
                .build();

        aluguelRepository.save(aluguel);

        return AluguelResponseDto.from(aluguel);

    }

    private Usuario validarUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(NotFoundException.forEmail(email));

        aluguelRepository.findAluguelAtivoByUsuarioId(usuario.getIdUsuario(), StatusAluguel.ATIVO)
                .ifPresent(a -> {
                    NotFoundException.forUsuarioAluguel();
                });

        return usuario;
    }

    private Moto validarMoto(Long motoId, Long usuarioId) {
        Moto moto = motoRepository.findById(motoId)
                .orElseThrow(NotFoundException.forMoto(motoId));

        if (!moto.getStatus().getStatus().equals(StatusEnum.ATIVO)) {
            throw new RuntimeException("Moto não disponível");
        }
        return moto;
    }


    private Double calcularValorTotal(LocalDate dataInicio, LocalDate dataFim) {
        long dias = Math.abs(ChronoUnit.DAYS.between(dataInicio, dataFim));

        long diasAlugados = dias + 1;

        double valorDiario = 50;

        return diasAlugados * valorDiario;
    }

    private void atualizarStatusAlugada(Moto moto) {
        StatusMoto statusMoto = statusMotoRepository.findByStatus(StatusEnum.ALUGADA)
                .orElseThrow(NotFoundException.forAluguel());

        moto.setStatus(statusMoto);
        motoRepository.save(moto);
    }

    StatusAluguel calculateStatus(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate hoje = LocalDate.now();

        if (hoje.isAfter(dataFim)) {
            return StatusAluguel.INATIVO;
        }


        if (hoje.isEqual(dataInicio) || hoje.isAfter(dataInicio)) {
            return StatusAluguel.ATIVO;
        }

        return StatusAluguel.PENDENTE;
    }

    public List<AluguelResponseDto> getAll() {
        return aluguelRepository.findAll()
                .stream()
                .map(AluguelResponseDto::from)
                .toList();
    }


    @Transactional
    @Scheduled(fixedRate = 60000)
    public void atualizarAlugueisVencidos() {
        List<Aluguel> aluguelList = aluguelRepository.findAll();

        for(Aluguel aluguel: aluguelList) {
            if (aluguel.getStatusAluguel() == StatusAluguel.ATIVO && aluguel.getDataFim().isBefore(LocalDate.now())) {
                aluguel.setStatusAluguel(StatusAluguel.INATIVO);
                aluguelRepository.save(aluguel);
            }
        }
    }

    public List<AluguelResponseDto> findByUsuarioEmail(String email) {
        return aluguelRepository.findByUsuarioEmail(email).stream().map(AluguelResponseDto::from).toList();
    }

    @Transactional
    public void delete(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(NotFoundException.forAluguel());

        if (aluguel.getStatusAluguel() == StatusAluguel.ATIVO) {
            throw new IllegalStateException("Não é possível excluir aluguel com status ATIVO. O aluguel deve ser INATIVO.");
        }

        Moto moto = aluguel.getMoto();

        atualizarStatusAtiva(moto);

        aluguelRepository.delete(aluguel);
    }

    private void atualizarStatusAtiva(Moto moto) {
        StatusMoto statusMoto = statusMotoRepository.findByStatus(StatusEnum.ATIVO)
                .orElseThrow(() -> new RuntimeException("Status ATIVO/DISPONÍVEL não encontrado no banco de dados."));

        moto.setStatus(statusMoto);
        motoRepository.save(moto);
    }

}




