package com.proyecto.plataformas.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("usuarios")
public class UsuarioEntity {
    @Id
    private Long id;

    private String correo;
    private String nombreCompleto;
    private String telefono;
    private String numeroDocumento;
    private LocalDateTime fechaCreacion;
    private String estado;
}
