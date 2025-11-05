package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Moto;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import br.com.fiap.smartmottu.entity.enuns.TipoMotoEnum;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MotoResponseDto {

    private Long idMoto;
    private String nmChassi;
    private String placa;
    private String unidade;
    private StatusEnum statusId;
    private TipoMotoEnum modeloId;

    public static MotoResponseDto from(Moto moto) {
        return MotoResponseDto.builder()
                .nmChassi(moto.getNmChassi())
                .placa(moto.getPlaca())
                .unidade(moto.getUnidade())
                .statusId(moto.getStatus() != null ? moto.getStatus().getStatus() : null)
                .modeloId(moto.getModelo() != null ? moto.getModelo().getNmTipo() : null)
                .build();
    }
}
