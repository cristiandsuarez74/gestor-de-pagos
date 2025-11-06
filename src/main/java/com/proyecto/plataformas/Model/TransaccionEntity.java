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
@Table("transacciones")
public class TransaccionEntity {

    @Id
    private Long id;
    @Column("cuenta_origen_id")
    private Long cuentaOrigenId;
    @Column("cuenta_destino_id")
    private Long cuentaDestinoId;
    private BigDecimal monto;
    private String tipoTransaccion;
    private String descripcion;
    private LocalDateTime fechaTransaccion;
    private String estado;
    private String codigoReferencia;

}
