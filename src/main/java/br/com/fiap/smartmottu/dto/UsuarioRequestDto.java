package br.com.fiap.smartmottu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UsuarioRequestDto {

    @NotBlank
    private String nome;

    @NotBlank @Email
    private String email;

    @Size(min = 8, max = 15)
    @NotBlank
    private String senha;
}
