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
    // Rótulos legíveis para exibição nas telas
    private String statusName;
    private String modeloName;


    public static MotoResponseDto from(Moto moto) {
        return MotoResponseDto.builder()
                .idMoto(moto.getIdMoto())
                .nmChassi(moto.getNmChassi())
                .placa(moto.getPlaca())
                .unidade(moto.getUnidade())
                .statusId(moto.getStatus() != null ? moto.getStatus().getIdStatus() : null)
                .modeloId(moto.getModelo() != null ? moto.getModelo().getIdTipo() : null)
        .statusName(moto.getStatus() != null && moto.getStatus().getStatus() != null
            ? moto.getStatus().getStatus().getDescricao() : null)
        .modeloName(moto.getModelo() != null && moto.getModelo().getNmTipo() != null
            ? moto.getModelo().getNmTipo().getDescricao() : null)
                .build();
    }


}
