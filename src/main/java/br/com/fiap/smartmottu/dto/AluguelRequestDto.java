package br.com.fiap.smartmottu.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AluguelRequestDto {

    private String email;
    private Long motoId;
    private int dias;

}