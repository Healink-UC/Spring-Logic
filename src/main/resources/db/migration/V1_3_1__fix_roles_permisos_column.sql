-- Convertir la columna permisos a tipo jsonb
ALTER TABLE roles ALTER COLUMN permisos TYPE jsonb USING permisos::jsonb; 