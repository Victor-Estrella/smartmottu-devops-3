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
public class LoginRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 15)
    private String senha;
}
