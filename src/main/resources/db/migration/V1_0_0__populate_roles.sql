
-- Agregar TODOS los roles nuevos (administrador, desarrollador, entidad_salud, medico, auxiliar, paciente, embajador)
INSERT INTO roles (id, nombre, descripcion, creado_por, fecha_creacion, permisos)
VALUES 
(1, 'administrador', 'Administrador del sistema', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(2, 'desarrollador', 'Desarrollador técnico del aplicativo', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(3, 'entidad_salud', 'Representante de entidad de salud', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(4, 'medico', 'Profesional médico de la salud', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(5, 'auxiliar', 'Personal auxiliar médico', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(6, 'paciente', 'Paciente o usuario de servicios', 'administrador', CURRENT_TIMESTAMP, '{"escritura":true, "lectura":true, "actualizacion":true, "eliminacion":true}'),
(7, 'embajador', 'Embajador comunitario', 'administrador', CURRENT_TIMESTAMP, '{}');

-- Ver la tabla roles
SELECT * FROM roles;
