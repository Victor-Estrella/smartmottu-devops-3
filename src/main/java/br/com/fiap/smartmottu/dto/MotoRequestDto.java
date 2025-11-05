package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import br.com.fiap.smartmottu.entity.enuns.TipoMotoEnum;
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

    private StatusEnum statusId;
    private TipoMotoEnum modeloId;


    @NotBlank
    private String unidade;

}
