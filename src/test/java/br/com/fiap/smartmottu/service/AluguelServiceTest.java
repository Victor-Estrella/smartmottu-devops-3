package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.AluguelRequestDto;
import br.com.fiap.smartmottu.entity.Aluguel;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
import br.com.fiap.smartmottu.exception.NotFoundException;
import br.com.fiap.smartmottu.repository.AluguelRepository;
import br.com.fiap.smartmottu.repository.MotoRepository;
import br.com.fiap.smartmottu.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluguelServiceTest {

    @InjectMocks
    private AluguelService aluguelService;


    @Mock
    private AluguelRepository aluguelRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MotoRepository motoRepository;


    private AluguelRequestDto requestDto;
    private Usuario usuarioAtivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        requestDto = AluguelRequestDto.builder()
                .email("user@test.com")
                .motoId(10L)
                .dias(5)
                .build();



        usuarioAtivo = Usuario.builder()
                .idUsuario(1L)
                .nome("User Teste")
                .email("user@test.com")
                .build();

    }


    @Test
    @DisplayName("Deve lançar NotFoundException se o Usuário não existir")
    void alugarMoto_UserNotFound() {
        when(usuarioRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> aluguelService.alugarMoto(requestDto));


        verify(aluguelRepository, never()).save(any(Aluguel.class));
    }


    @Test
    @DisplayName("Deve lançar NotFoundException se a Moto não existir")
    void alugarMoto_MotoNotFound() {

        when(usuarioRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(usuarioAtivo));
        when(aluguelRepository.findAluguelAtivoByUsuarioId(anyLong(), eq(StatusAluguel.ATIVO))).thenReturn(Optional.empty());

        when(motoRepository.findById(requestDto.getMotoId())).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> aluguelService.alugarMoto(requestDto));
    }




}
