package com.grupo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "patio",
        indexes = {
                @Index(name = "idx_patio_nome", columnList = "nome"),
                @Index(name = "idx_patio_cidade", columnList = "cidade")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patio")
    @EqualsAndHashCode.Include
    private Long idPatio;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Size(max = 100)
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    // Pode ser grande: usar CLOB/text
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "layout")
    private String layout;
}
