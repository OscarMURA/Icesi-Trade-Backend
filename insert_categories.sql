-- Script para insertar categorías en Icesi Trade
-- Ejecutar después de crear las tablas
-- Limpiar categorías existentes (opcional)
-- DELETE FROM CATEGORIES;
-- Insertar categorías principales
INSERT INTO CATEGORIES (name, description)
VALUES (
        'Electrónicos',
        'Dispositivos electrónicos, smartphones, laptops, tablets y accesorios'
    ),
    (
        'Libros',
        'Libros de texto, novelas, manuales y material académico'
    ),
    (
        'Ropa',
        'Ropa casual, formal, deportiva y accesorios'
    ),
    (
        'Deportes',
        'Equipos deportivos, ropa deportiva y accesorios'
    ),
    (
        'Hogar',
        'Artículos para el hogar, decoración y muebles'
    ),
    (
        'Transporte',
        'Bicicletas, scooters, patinetas y accesorios'
    ),
    (
        'Música',
        'Instrumentos musicales, equipos de audio y partituras'
    ),
    (
        'Arte',
        'Materiales de arte, pinturas, pinceles y lienzos'
    ),
    (
        'Cocina',
        'Utensilios de cocina, electrodomésticos y recetas'
    ),
    (
        'Jardín',
        'Plantas, macetas, herramientas de jardín y decoración'
    ),
    (
        'Tecnología',
        'Componentes de computadora, software y gadgets'
    ),
    (
        'Fotografía',
        'Cámaras, lentes, trípodes y accesorios fotográficos'
    ),
    (
        'Juegos',
        'Videojuegos, juegos de mesa y juguetes'
    ),
    (
        'Salud',
        'Productos de cuidado personal, vitaminas y suplementos'
    ),
    (
        'Viajes',
        'Maletas, mochilas y accesorios de viaje'
    ),
    (
        'Mascotas',
        'Alimentos, juguetes y accesorios para mascotas'
    ),
    (
        'Oficina',
        'Material de oficina, papelería y mobiliario'
    ),
    (
        'Fitness',
        'Equipos de ejercicio, suplementos y ropa deportiva'
    ),
    (
        'Belleza',
        'Cosméticos, productos de cuidado personal y accesorios'
    ),
    (
        'Otros',
        'Productos diversos que no encajan en otras categorías'
    );
-- Verificar las categorías insertadas
SELECT *
FROM CATEGORIES
ORDER BY name;