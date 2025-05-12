-- Añadir columna actividad_fisica a la tabla triajes
ALTER TABLE triajes ADD COLUMN IF NOT EXISTS actividad_fisica boolean NOT NULL DEFAULT false;