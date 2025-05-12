-- Eliminar columna estado de la tabla PACIENTES ya que es redundante con el estado del usuario
ALTER TABLE PACIENTES DROP COLUMN IF EXISTS estado; 