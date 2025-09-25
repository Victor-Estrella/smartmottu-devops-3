package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Moto;
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
    private Long statusId;
    private Long modeloId;


    public static MotoResponseDto from(Moto moto) {
        return MotoResponseDto.builder()
                .idMoto(moto.getIdMoto())
                .nmChassi(moto.getNmChassi())
                .placa(moto.getPlaca())
                .unidade(moto.getUnidade())
                .statusId(moto.getStatus() != null ? moto.getStatus().getIdStatus() : null)
                .modeloId(moto.getModelo() != null ? Long.valueOf(moto.getModelo().getNmTipo().getDescricao()) : null)
                .build();
    }


}
