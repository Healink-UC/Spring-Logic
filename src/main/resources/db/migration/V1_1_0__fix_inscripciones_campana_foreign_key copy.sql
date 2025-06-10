-- Arreglar foreign key constraint de inscripciones_campana para que apunte a usuarios
-- en lugar de pacientes

-- 1. Eliminar el constraint viejo que apunta a pacientes
ALTER TABLE inscripciones_campana 
DROP CONSTRAINT IF EXISTS inscripciones_campana_paciente_id_fkey;

-- 2. Crear el nuevo constraint que apunta a usuarios
ALTER TABLE inscripciones_campana 
ADD CONSTRAINT inscripciones_campana_usuario_id_fkey 
FOREIGN KEY (usuario_id) REFERENCES usuarios(id);

-- 3. Actualizar el constraint unique para usar usuario_id
ALTER TABLE inscripciones_campana 
DROP CONSTRAINT IF EXISTS inscripciones_campana_paciente_id_campana_id_key;

ALTER TABLE inscripciones_campana 
ADD CONSTRAINT inscripciones_campana_usuario_id_campana_id_key 
UNIQUE (usuario_id, campana_id); 