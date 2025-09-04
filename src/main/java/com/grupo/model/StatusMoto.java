package com.grupo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "status_moto",
        indexes = @Index(name = "idx_status_descricao", columnList = "descricao"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StatusMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    @EqualsAndHashCode.Include
    private Long idStatus;

    @NotBlank
    @Size(max = 50)
    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;
}
