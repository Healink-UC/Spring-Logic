-- Agregar columna hora_fin_atencion a la tabla CITACIONES_MEDICAS
-- para registrar cuando termina la atención médica

ALTER TABLE CITACIONES_MEDICAS 
ADD COLUMN hora_fin_atencion TIMESTAMP NULL;

-- Comentario para documentar el campo
COMMENT ON COLUMN CITACIONES_MEDICAS.hora_fin_atencion IS 'Hora y fecha en que termina la atención médica del paciente';

-- Agregar índice para consultas de rendimiento si es necesario
CREATE INDEX IF NOT EXISTS idx_citaciones_hora_fin_atencion 
ON CITACIONES_MEDICAS(hora_fin_atencion) 
WHERE hora_fin_atencion IS NOT NULL; 