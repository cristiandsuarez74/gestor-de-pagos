-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios
(
    id bigserial PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVO'
);

-- Tabla cuentas
CREATE TABLE IF NOT EXISTS cuentas
(
    id bigserial PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    moneda VARCHAR(3) DEFAULT 'COP',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    CONSTRAINT cuentas_usuario_id_fkey FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id),
    CONSTRAINT saldo_positivo CHECK (saldo >= 0)
);

-- Tabla transacciones
CREATE TABLE IF NOT EXISTS transacciones
(
    id bigserial PRIMARY KEY,
    cuenta_origen_id BIGINT NOT NULL,
    cuenta_destino_id BIGINT NOT NULL,
    monto NUMERIC(15,2) NOT NULL,
    tipo_transaccion VARCHAR(20) NOT NULL,
    descripcion VARCHAR(200),
    fecha_transaccion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL,
    codigo_referencia VARCHAR(50) UNIQUE,
    CONSTRAINT transacciones_cuenta_origen_id_fkey FOREIGN KEY (cuenta_origen_id)
        REFERENCES cuentas (id),
    CONSTRAINT transacciones_cuenta_destino_id_fkey FOREIGN KEY (cuenta_destino_id)
        REFERENCES cuentas (id),
    CONSTRAINT monto_positivo CHECK (monto > 0),
    CONSTRAINT cuentas_diferentes CHECK (cuenta_origen_id <> cuenta_destino_id)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_cuentas_usuario ON cuentas(usuario_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_origen ON transacciones(cuenta_origen_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_destino ON transacciones(cuenta_destino_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_fecha ON transacciones(fecha_transaccion);
CREATE INDEX IF NOT EXISTS idx_transacciones_estado ON transacciones(estado);