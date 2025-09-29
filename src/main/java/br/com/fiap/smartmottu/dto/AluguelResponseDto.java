package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Aluguel;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AluguelResponseDto {

    private Long aluguelId;
    private String usuario;
    private String moto;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorTotal;


    public static AluguelResponseDto from(Aluguel aluguel) {
        return AluguelResponseDto.builder()
                .aluguelId(aluguel.getId())
                .usuario(aluguel.getUsuario().getNome())
                .moto(aluguel.getMoto().getNmChassi())
                .status(aluguel.getStatusAluguel().name())
                .dataInicio(aluguel.getDataInicio())
                .dataFim(aluguel.getDataFim())
                .valorTotal(aluguel.getValorTotal())
                .build();
    }

}