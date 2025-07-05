-- Script para insertar categorías específicas para estudiantes universitarios
-- Icesi Trade - Categorías orientadas al comercio universitario
-- Limpiar categorías existentes (opcional)
-- DELETE FROM CATEGORIES;
-- Insertar categorías específicas para estudiantes
INSERT INTO CATEGORIES (name, description)
VALUES (
        'Libros de Texto',
        'Libros de texto universitarios, manuales y material de estudio'
    ),
    (
        'Electrónicos',
        'Laptops, tablets, smartphones y accesorios tecnológicos'
    ),
    (
        'Material Académico',
        'Cuadernos, calculadoras, mochilas y útiles escolares'
    ),
    (
        'Ropa Universitaria',
        'Ropa casual, formal para presentaciones y uniformes'
    ),
    (
        'Deportes',
        'Equipos deportivos, ropa deportiva y accesorios'
    ),
    (
        'Hogar Estudiantil',
        'Artículos para dormitorio, cocina y decoración'
    ),
    (
        'Transporte',
        'Bicicletas, scooters, patinetas y accesorios'
    ),
    (
        'Música y Arte',
        'Instrumentos musicales, materiales de arte y manualidades'
    ),
    (
        'Cocina Estudiantil',
        'Utensilios de cocina, electrodomésticos pequeños'
    ),
    (
        'Tecnología',
        'Componentes de PC, software y gadgets tecnológicos'
    ),
    (
        'Fotografía',
        'Cámaras, lentes y accesorios fotográficos'
    ),
    (
        'Juegos y Entretenimiento',
        'Videojuegos, juegos de mesa y entretenimiento'
    ),
    (
        'Salud y Bienestar',
        'Productos de cuidado personal, vitaminas y suplementos'
    ),
    (
        'Viajes y Turismo',
        'Maletas, mochilas y accesorios de viaje'
    ),
    (
        'Mascotas',
        'Alimentos, juguetes y accesorios para mascotas'
    ),
    (
        'Oficina y Papelería',
        'Material de oficina, papelería y mobiliario'
    ),
    (
        'Fitness y Ejercicio',
        'Equipos de ejercicio, suplementos y ropa deportiva'
    ),
    (
        'Belleza y Cuidado',
        'Cosméticos, productos de cuidado personal'
    ),
    (
        'Servicios',
        'Servicios de tutoría, diseño, programación y otros'
    ),
    (
        'Otros',
        'Productos diversos que no encajan en otras categorías'
    );
-- Verificar las categorías insertadas
SELECT id,
    name,
    description
FROM CATEGORIES
ORDER BY name;