package br.com.fiap.smartmottu.controller;

import br.com.fiap.smartmottu.dto.LoginRequestDto;
import br.com.fiap.smartmottu.dto.LoginResponse;
import br.com.fiap.smartmottu.dto.LoginUsuarioResponseDto;
import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.security.JWTUtil;
import br.com.fiap.smartmottu.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class AutenticacaoController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {

            var authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getSenha()
            );
            authenticationManager.authenticate(authToken);

            String token = jwtUtil.construirToken(loginRequest.getEmail(), loginRequest.getSenha());


            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

            LoginUsuarioResponseDto usuarioInfo = LoginUsuarioResponseDto
                    .builder()
                    .idUsuario(usuario.getIdUsuario())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .build();

            LoginResponse response = LoginResponse
                    .builder()
                    .token(token)
                    .usuario(usuarioInfo)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
