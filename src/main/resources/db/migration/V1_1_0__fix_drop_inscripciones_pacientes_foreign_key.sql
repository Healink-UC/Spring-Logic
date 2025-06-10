-- Obtener las restricciones de la tabla inscripciones_campana
SELECT * FROM information_schema.table_constraints WHERE table_name = 'inscripciones_campana';

-- Eliminar la restricción existentes y que se añadan de nuevo al ejecutar el backend
ALTER TABLE inscripciones_campana
DROP CONSTRAINT IF EXISTS inscripciones_campana_paciente_id_fkey;
ALTER TABLE inscripciones_campana
DROP CONSTRAINT IF EXISTS inscripciones_campana_paciente_id_campana_id_key;

ALTER TABLE inscripciones_campana
DROP CONSTRAINT IF EXISTS inscripciones_campana_usuario_id_campana_id_key;
