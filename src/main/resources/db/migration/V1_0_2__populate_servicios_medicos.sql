-- Añadir los servicios médicos de la tabla servicios_medicos en correlación al riesgo cardiovascular
INSERT INTO servicios_medicos (id, nombre, descripcion, creado_por, fecha_creacion)
VALUES 
(1, 'Servicio de medicina interna', 'Servicio de medicina interna', 'personal_medico', CURRENT_TIMESTAMP),
(2, 'Servicio de cardiología', 'Servicio de cardiología', 'personal_medico', CURRENT_TIMESTAMP),
(3, 'Servicio de cirugía cardiovascular', 'Servicio de cirugía cardiovascular', 'personal_medico', CURRENT_TIMESTAMP),
(4, 'Servicio de medicina nuclear', 'Servicio de medicina nuclear', 'personal_medico', CURRENT_TIMESTAMP),
(5, 'Servicio de radiología', 'Servicio de radiología', 'personal_medico', CURRENT_TIMESTAMP);
