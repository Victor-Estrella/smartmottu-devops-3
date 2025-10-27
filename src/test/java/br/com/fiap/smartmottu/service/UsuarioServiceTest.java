package br.com.fiap.smartmottu.service;

import br.com.fiap.smartmottu.dto.UsuarioRequestDto;
import br.com.fiap.smartmottu.dto.UsuarioResponseDto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.entity.enuns.RoleEnum;
import br.com.fiap.smartmottu.repository.AluguelRepository;
import br.com.fiap.smartmottu.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AluguelRepository aluguelRepository;

    private UsuarioRequestDto requestDto;
    private final String RAW_PASSWORD = "senha123";
    private final String ENCODED_PASSWORD = "$2a$10$encodedPasswordHash";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = UsuarioRequestDto.builder()
                .nome("Sofia Petruk")
                .email("soso@email.com")
                .senha(RAW_PASSWORD)
                .build();
    }

    @Test
    @DisplayName("Deve salvar o usuário com sucesso, criptografando a senha e definindo a Role padrão")
    void save_shouldSaveUserWithEncodedPasswordAndDefaultRole() {

        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        Usuario usuarioSaved = Usuario.builder()
                .idUsuario(1L)
                .nome("Sofia Petruk")
                .email("soso@email.com")
                .senha(ENCODED_PASSWORD)
                .role(RoleEnum.USER)
                .build();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSaved);

        UsuarioResponseDto result = usuarioService.save(requestDto);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(1)).save(usuarioCaptor.capture());

        verify(passwordEncoder, times(1)).encode(RAW_PASSWORD);
    }

}
