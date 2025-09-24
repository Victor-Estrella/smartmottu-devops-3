package br.com.fiap.smartmottu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "T_SMARTMOTTU_STATUS_MOTO")
@SequenceGenerator(name = "statusMoto", sequenceName = "SQ_T_SMARTMOTTU_STATUS_MOTO", allocationSize = 1)

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StatusMoto {

    @Id
    @Column(name = "id_status")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusMoto")
    private Long idStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "data")
    private LocalDate data;

}