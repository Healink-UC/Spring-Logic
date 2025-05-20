-- Añadir columnas direccion y tipo_sangre a la tabla PACIENTES
ALTER TABLE PACIENTES 
    ADD COLUMN IF NOT EXISTS direccion VARCHAR(255),
    ADD COLUMN IF NOT EXISTS tipo_sangre VARCHAR(20); 