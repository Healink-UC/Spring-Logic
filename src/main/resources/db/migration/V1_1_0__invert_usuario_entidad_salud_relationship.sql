-- Migración para invertir la relación entre Usuario y EntidadSalud
-- Ahora EntidadSalud (1) -> Usuario (muchos)

-- Paso 1: Agregar columna entidad_salud_id a la tabla USUARIOS
ALTER TABLE USUARIOS ADD COLUMN entidad_salud_id INTEGER;

-- Paso 2: Crear la clave foránea
ALTER TABLE USUARIOS ADD CONSTRAINT fk_usuarios_entidad_salud 
    FOREIGN KEY (entidad_salud_id) REFERENCES ENTIDADES_SALUD(id);

-- Paso 3: Migrar los datos existentes
-- Actualizar usuarios con su entidad de salud correspondiente
UPDATE USUARIOS 
SET entidad_salud_id = (
    SELECT id 
    FROM ENTIDADES_SALUD 
    WHERE ENTIDADES_SALUD.usuario_id = USUARIOS.id
    LIMIT 1
)
WHERE EXISTS (
    SELECT 1 
    FROM ENTIDADES_SALUD 
    WHERE ENTIDADES_SALUD.usuario_id = USUARIOS.id
);

-- Paso 4: Eliminar la relación antigua
ALTER TABLE ENTIDADES_SALUD DROP CONSTRAINT IF EXISTS fk_entidades_salud_usuario;
ALTER TABLE ENTIDADES_SALUD DROP COLUMN IF EXISTS usuario_id;

-- Paso 5: Crear índice para mejorar el rendimiento
CREATE INDEX idx_usuarios_entidad_salud ON USUARIOS(entidad_salud_id);

