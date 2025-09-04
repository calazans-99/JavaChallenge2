package com.grupo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
        name = "moto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_moto_placa", columnNames = "placa")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moto")
    @EqualsAndHashCode.Include
    private Long idMoto;

    @NotBlank
    @Size(min = 7, max = 8)
    @Column(name = "placa", nullable = false, length = 8)
    private String placa;

    @NotBlank
    @Size(max = 50)
    @Column(name = "modelo", length = 50)
    private String modelo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_patio", nullable = false,
            foreignKey = @ForeignKey(name = "fk_moto_patio"))
    private Patio patio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false,
            foreignKey = @ForeignKey(name = "fk_moto_status"))
    private StatusMoto statusMoto;

    @Column(name = "pos_x")
    private Integer posX;

    @Column(name = "pos_y")
    private Integer posY;
}
