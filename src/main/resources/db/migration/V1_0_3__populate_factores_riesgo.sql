-- Añadir los factores de riesgo de la tabla factores_riesgo

-- Tipos de factores de riesgo:
-- SOCIAL
-- AMBIENTAL
-- RACIAL
INSERT INTO factores_riesgo (id, nombre, descripcion, creado_por, fecha_creacion, tipo)
VALUES 
(1, 'Tabaquismo', 'Tabaquismo', 'personal_medico', CURRENT_TIMESTAMP, 'SOCIAL'),
(2, 'Obesidad', 'Obesidad', 'personal_medico', CURRENT_TIMESTAMP, 'AMBIENTAL'),
(3, 'Hipertensión', 'Hipertensión', 'personal_medico', CURRENT_TIMESTAMP, 'SOCIAL'),
(4, 'Diabetes', 'Diabetes', 'personal_medico', CURRENT_TIMESTAMP, 'RACIAL'),
(5, 'Colesterol', 'Colesterol', 'personal_medico', CURRENT_TIMESTAMP, 'SOCIAL'),
(6, 'Presión arterial alta', 'Presión arterial alta', 'personal_medico', CURRENT_TIMESTAMP, 'RACIAL');