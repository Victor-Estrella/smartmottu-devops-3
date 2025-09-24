package br.com.fiap.smartmottu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "T_SMARTMOTTU_TIPO_MOTO")
@SequenceGenerator(name = "tipoMoto", sequenceName = "SQ_T_SMARTMOTTU_TIPO_MOTO", allocationSize = 1)


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TipoMoto {

    @Id
    @Column(name = "id_tipo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoMoto")
    private Long idTipo;

    @Column(name = "nm_tipo")
    private String nmTipo;
}