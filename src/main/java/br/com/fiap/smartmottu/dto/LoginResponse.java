package br.com.fiap.smartmottu.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LoginResponse {

    private String token;
    private LoginUsuarioResponseDto usuario;

}
