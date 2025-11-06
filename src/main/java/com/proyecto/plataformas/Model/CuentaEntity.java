package com.proyecto.plataformas.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("cuentas")
public class CuentaEntity {
    @Id
    private Long id;
    @Column("usuario_id")
    private Long usuarioId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private String moneda;
    private LocalDateTime fechaCreacion;
    private String estado;
}
