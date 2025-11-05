package br.com.fiap.smartmottu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AluguelRequestDto {

    private String email;
    private Long motoId;
    private int dias;

    @NotNull(message = "A data de início é obrigatória.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

}
