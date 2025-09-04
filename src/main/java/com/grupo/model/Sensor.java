package com.grupo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "sensor",
        indexes = @Index(name = "idx_sensor_moto", columnList = "id_moto"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor")
    @EqualsAndHashCode.Include
    private Long idSensor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_moto", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sensor_moto"))
    private Moto moto;

    @DecimalMin(value = "-100.0")
    @DecimalMax(value = "200.0")
    @Column(name = "temperatura")
    private Double temperatura;

    @NotNull
    @Pattern(regexp = "^[SN]$")
    @Column(name = "ligada", nullable = false, length = 1)
    private String ligada; // 'S' ou 'N'
}
