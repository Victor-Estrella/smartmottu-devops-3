package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Usuario;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UsuarioResponseDto {

    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;

    public static UsuarioResponseDto from(Usuario u) {
        return UsuarioResponseDto.builder()
                .idUsuario(u.getIdUsuario())
                .nome(u.getNome())
                .email(u.getEmail())
                .senha(u.getSenha())
                .build();
    }

    //fazer com que retorne a a data sozinho de quando foi criado o usuario
    //precisa ter paginação
    //precisa fazer o swagger

}
