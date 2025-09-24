package br.com.fiap.smartmottu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MotoRequestDto {

    @NotBlank
    @Size(min = 17, max = 17)
    private String nmChassi;

    @NotBlank @Size(min = 7, max = 7)
    private String placa;

    private Long statusId;

    private Long modeloId;

    @NotBlank
    private String unidade;

}
