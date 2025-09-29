package br.com.fiap.smartmottu.entity;

import br.com.fiap.smartmottu.entity.enuns.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "T_SMARTMOTTU_USUARIO")
@SequenceGenerator(name = "usuario", sequenceName = "SQ_T_SMARTMOTTU_USUARIO", allocationSize = 1)

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario")
    private Long idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

}