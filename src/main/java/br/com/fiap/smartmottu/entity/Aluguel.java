package br.com.fiap.smartmottu.entity;

import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "T_SMARTMOTTU_ALUGUEL")
@SequenceGenerator(name = "aluguel", sequenceName = "SQ_T_SMARTMOTTU_ALUGUEL", allocationSize = 1)

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluguel")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_moto_id", nullable = false)
    private Moto moto;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusAluguel statusAluguel;
}
