
package br.com.fiap.smartmottu.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LoginUsuarioResponseDto {

    private Long idUsuario;
    private String nome;
    private String email;

}
