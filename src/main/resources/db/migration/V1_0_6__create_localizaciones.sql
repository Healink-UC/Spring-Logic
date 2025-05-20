-- Crear tabla para almacenar localizaciones rurales de Caldas, Colombia
CREATE TABLE IF NOT EXISTS LOCALIZACIONES (
    id SERIAL PRIMARY KEY,
    departamento VARCHAR(100) NOT NULL,
    municipio VARCHAR(100) NOT NULL,
    vereda VARCHAR(100) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    latitud DOUBLE PRECISION NOT NULL,
    longitud DOUBLE PRECISION NOT NULL,
    actualizado_por VARCHAR(255),
    creado_por VARCHAR(255),
    fecha_actualizacion TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Asegurar que no haya duplicados de la misma ubicación
    CONSTRAINT uk_localizacion UNIQUE (departamento, municipio, vereda, localidad)
);

-- Crear índices para búsquedas comunes
CREATE INDEX idx_localizacion_departamento ON LOCALIZACIONES (departamento);
CREATE INDEX idx_localizacion_municipio ON LOCALIZACIONES (municipio);
CREATE INDEX idx_localizacion_vereda ON LOCALIZACIONES (vereda);

-- Comentario de la tabla
COMMENT ON TABLE LOCALIZACIONES IS 'Almacena información de ubicaciones rurales en Caldas, Colombia, incluyendo coordenadas geográficas'; 