-- Poblar tabla LOCALIZACIONES con datos de veredas rurales de Caldas
INSERT INTO LOCALIZACIONES (departamento, municipio, vereda, localidad, latitud, longitud)
VALUES 
    ('Caldas', 'Manizales', 'Eucalipto', 'Sector Sur', 5.0534, -75.4894),
    ('Caldas', 'Manizales', 'La Linda', 'Sector Alto', 5.0679, -75.5174),
    ('Caldas', 'Manizales', 'El Arenillo', 'Zona Central', 5.0551, -75.4989),
    ('Caldas', 'Manizales', 'La Cabaña', 'Sector Principal', 5.0412, -75.5217),
    
    ('Caldas', 'Chinchiná', 'El Trébol', 'Zona Cafetera', 4.9841, -75.6072),
    ('Caldas', 'Chinchiná', 'La Floresta', 'Sector Alto', 4.9937, -75.6183),
    ('Caldas', 'Chinchiná', 'El Edén', 'Zona Rural', 4.9762, -75.6094),
    
    ('Caldas', 'Villamaría', 'Llanitos', 'Sector Principal', 5.0412, -75.5134),
    ('Caldas', 'Villamaría', 'La Guayana', 'Zona Alta', 5.0321, -75.5246),
    ('Caldas', 'Villamaría', 'Santo Domingo', 'Sector Rural', 5.0234, -75.5189),
    
    ('Caldas', 'Neira', 'El Río', 'Zona Baja', 5.1668, -75.5192),
    ('Caldas', 'Neira', 'San Pablo', 'Sector Central', 5.1589, -75.5237),
    ('Caldas', 'Neira', 'La Mesa', 'Área Rural', 5.1723, -75.5154),
    
    ('Caldas', 'Palestina', 'Santágueda', 'Zona Turística', 5.0912, -75.6234),
    ('Caldas', 'Palestina', 'El Higuerón', 'Sector Cafetero', 5.0823, -75.6189),
    ('Caldas', 'Palestina', 'La Plata', 'Área Principal', 5.0867, -75.6278);

COMMENT ON TABLE LOCALIZACION IS 'Datos iniciales de localizaciones rurales en municipios principales de Caldas'; 