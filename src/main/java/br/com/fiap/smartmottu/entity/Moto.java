package br.com.fiap.smartmottu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "T_SMARTMOTTU_MOTO")
@SequenceGenerator(name = "moto", sequenceName = "SQ_T_SMARTMOTTU_MOTO", allocationSize = 1)

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moto")
    @Column(name = "id_moto")
    private Long idMoto;

    @Column(name = "nm_chassi", length = 17, nullable = false)
    private String nmChassi;

    @Column(name = "placa", length = 7, nullable = false)
    private String placa;

    @Column(name = "unidade", length = 100)
    private String unidade;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_id_status")
    private StatusMoto status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fk_id_tipo")
    private TipoMoto modelo;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;



}