-- Script para limpiar relaciones fantasma después de eliminar atenciones_medicas
-- =========================================================================

-- 1. Verificar registros huérfanos en DIAGNOSTICOS
SELECT 'DIAGNOSTICOS con atencion_id inexistente:' as tabla;
SELECT d.id, d.atencion_id 
FROM DIAGNOSTICOS d 
LEFT JOIN ATENCIONES_MEDICAS a ON d.atencion_id = a.id 
WHERE a.id IS NULL;

-- 2. Verificar registros huérfanos en SEGUIMIENTOS  
SELECT 'SEGUIMIENTOS con atencion_id inexistente:' as tabla;
SELECT s.id, s.atencion_id 
FROM SEGUIMIENTOS s 
LEFT JOIN ATENCIONES_MEDICAS a ON s.atencion_id = a.id 
WHERE a.id IS NULL;

-- 3. Verificar si existe la tabla PERSONAL_MEDICO
SELECT 'Verificando tabla PERSONAL_MEDICO:' as verificacion;
SELECT CASE 
    WHEN COUNT(*) > 0 THEN 'EXISTE' 
    ELSE 'NO EXISTE' 
END as estado
FROM information_schema.tables 
WHERE table_name = 'PERSONAL_MEDICO';

-- 4. Verificar referencias huérfanas a personal_medico en CITACIONES_MEDICAS
SELECT 'CITACIONES_MEDICAS con medico_id que apuntaba a PERSONAL_MEDICO:' as tabla;
SELECT c.id, c.medico_id, c.estado, c.fecha_creacion
FROM CITACIONES_MEDICAS c 
WHERE c.medico_id IS NOT NULL;

-- =========================================================================
-- LIMPIEZA DE DATOS (DESCOMENTA LAS SIGUIENTES LÍNEAS PARA EJECUTAR)
-- =========================================================================

-- PASO 1: Actualizar DIAGNOSTICOS para que apunten a citacion_id en lugar de atencion_id
/*
-- Crear columna temporal si no existe
ALTER TABLE DIAGNOSTICOS ADD COLUMN citacion_id_temp BIGINT;

-- Mapear atencion_id a citacion_id usando la relación
UPDATE DIAGNOSTICOS d 
SET citacion_id_temp = (
    SELECT a.citacion_id 
    FROM ATENCIONES_MEDICAS a 
    WHERE a.id = d.atencion_id
);

-- Verificar mapeo
SELECT d.id, d.atencion_id as old_atencion, d.citacion_id_temp as new_citacion 
FROM DIAGNOSTICOS d 
WHERE d.citacion_id_temp IS NOT NULL;

-- Eliminar la columna antigua y renombrar la nueva
ALTER TABLE DIAGNOSTICOS DROP COLUMN atencion_id;
ALTER TABLE DIAGNOSTICOS RENAME COLUMN citacion_id_temp TO citacion_id;
*/

-- PASO 2: Actualizar SEGUIMIENTOS para que apunten a citacion_id
/*
-- Crear columna temporal si no existe
ALTER TABLE SEGUIMIENTOS ADD COLUMN citacion_id_temp BIGINT;

-- Mapear atencion_id a citacion_id usando la relación
UPDATE SEGUIMIENTOS s 
SET citacion_id_temp = (
    SELECT a.citacion_id 
    FROM ATENCIONES_MEDICAS a 
    WHERE a.id = s.atencion_id
);

-- Verificar mapeo
SELECT s.id, s.atencion_id as old_atencion, s.citacion_id_temp as new_citacion 
FROM SEGUIMIENTOS s 
WHERE s.citacion_id_temp IS NOT NULL;

-- Eliminar la columna antigua y renombrar la nueva
ALTER TABLE SEGUIMIENTOS DROP COLUMN atencion_id;
ALTER TABLE SEGUIMIENTOS RENAME COLUMN citacion_id_temp TO citacion_id;
*/

-- PASO 3: Eliminar registros huérfanos si los hay
/*
-- Eliminar diagnósticos huérfanos (sin citación válida)
DELETE FROM DIAGNOSTICOS 
WHERE citacion_id NOT IN (SELECT id FROM CITACIONES_MEDICAS);

-- Eliminar seguimientos huérfanos (sin citación válida)
DELETE FROM SEGUIMIENTOS 
WHERE citacion_id NOT IN (SELECT id FROM CITACIONES_MEDICAS);
*/

-- PASO 4: Eliminar tabla ATENCIONES_MEDICAS si existe
/*
DROP TABLE IF EXISTS ATENCIONES_MEDICAS;
*/

-- PASO 5: Eliminar tabla PERSONAL_MEDICO si existe
/*
DROP TABLE IF EXISTS PERSONAL_MEDICO;
*/

-- PASO 6: Verificar integridad después de la limpieza
/*
SELECT 'VERIFICACIÓN FINAL:' as titulo;

SELECT 'Total DIAGNOSTICOS:' as tabla, COUNT(*) as cantidad 
FROM DIAGNOSTICOS;

SELECT 'Total SEGUIMIENTOS:' as tabla, COUNT(*) as cantidad 
FROM SEGUIMIENTOS;

SELECT 'DIAGNOSTICOS con citacion_id válida:' as tabla, COUNT(*) as cantidad 
FROM DIAGNOSTICOS d 
INNER JOIN CITACIONES_MEDICAS c ON d.citacion_id = c.id;

SELECT 'SEGUIMIENTOS con citacion_id válida:' as tabla, COUNT(*) as cantidad 
FROM SEGUIMIENTOS s 
INNER JOIN CITACIONES_MEDICAS c ON s.citacion_id = c.id;
*/

-- =========================================================================
-- INSTRUCCIONES DE USO:
-- 1. Ejecuta primero las consultas SELECT para ver qué datos tienes
-- 2. Descomenta y ejecuta los bloques /* */ uno por uno si quieres limpiar
-- 3. Siempre haz backup antes de ejecutar los DELETE o DROP
-- ========================================================================= 