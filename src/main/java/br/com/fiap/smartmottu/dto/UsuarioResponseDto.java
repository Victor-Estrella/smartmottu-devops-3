package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Usuario;
import br.com.fiap.smartmottu.entity.enuns.RoleEnum;
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
    private RoleEnum role;
    private boolean temAluguel;


    public static UsuarioResponseDto from(Usuario u, boolean temAluguel) {
        return UsuarioResponseDto.builder()
                .idUsuario(u.getIdUsuario())
                .nome(u.getNome())
                .email(u.getEmail())
                .senha(u.getSenha())
                .role(u.getRole())
                .temAluguel(temAluguel)
                .build();
    }

    public static UsuarioResponseDto from(Usuario usuario) {
        return from(usuario, false);
    }

    //fazer com que retorne a a data sozinho de quando foi criado o usuario
    //precisa ter paginação
    //precisa fazer o swagger

}