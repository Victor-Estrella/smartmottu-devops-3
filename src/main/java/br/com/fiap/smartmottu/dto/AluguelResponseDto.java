package br.com.fiap.smartmottu.dto;

import br.com.fiap.smartmottu.entity.Aluguel;
import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
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
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorTotal;
    private StatusAluguel statusAluguel;


    public static AluguelResponseDto from(Aluguel aluguel) {


        return AluguelResponseDto.builder()
                .aluguelId(aluguel.getId())
                .usuario(aluguel.getUsuario().getEmail())
                .moto(aluguel.getMoto().getNmChassi())
                .dataInicio(aluguel.getDataInicio())
                .dataFim(aluguel.getDataFim())
                .valorTotal(aluguel.getValorTotal())
                .statusAluguel(aluguel.getStatusAluguel())
                .build();
    }

}
