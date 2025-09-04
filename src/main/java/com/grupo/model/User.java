package com.grupo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_usuario_nome", columnList = "nome"),
                @Index(name = "idx_usuario_email", columnList = "email")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private String tipoUsuario; // "Administrador" | "Operador"
}
