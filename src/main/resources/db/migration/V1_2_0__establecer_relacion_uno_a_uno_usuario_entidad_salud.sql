-- Migración para establecer relación uno a uno entre Usuario y EntidadSalud
-- Ahora EntidadSalud (1) <-> Usuario (1)

-- Paso 1: Agregar restricción única en entidad_salud_id en la tabla USUARIOS
-- Esto asegura que cada EntidadSalud solo pueda tener un Usuario
ALTER TABLE USUARIOS ADD CONSTRAINT uk_usuarios_entidad_salud 
    UNIQUE (entidad_salud_id);

-- Paso 2: Verificar que no existan registros duplicados antes de aplicar la restricción
-- (Esta consulta es para verificación, no se ejecuta automáticamente)
-- SELECT entidad_salud_id, COUNT(*) as cantidad 
-- FROM USUARIOS 
-- WHERE entidad_salud_id IS NOT NULL 
-- GROUP BY entidad_salud_id 
-- HAVING COUNT(*) > 1;

-- Paso 3: Crear índice para mejorar el rendimiento de búsquedas
CREATE INDEX idx_usuarios_entidad_salud_unique ON USUARIOS(entidad_salud_id) 
WHERE entidad_salud_id IS NOT NULL;

