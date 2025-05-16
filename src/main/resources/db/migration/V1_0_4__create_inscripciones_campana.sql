-- Crear tabla de inscripciones a campañas
CREATE TABLE IF NOT EXISTS inscripciones_campana (
    id SERIAL PRIMARY KEY,
    paciente_id INTEGER NOT NULL REFERENCES pacientes(id),
    campana_id INTEGER NOT NULL REFERENCES campanas(id),
    fecha_inscripcion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'INSCRITO' CHECK (estado IN ('INSCRITO', 'RETIRADO')),
    motivo_retiro TEXT,
    actualizado_por VARCHAR(255),
    creado_por VARCHAR(255),
    fecha_actualizacion TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(paciente_id, campana_id)
); 