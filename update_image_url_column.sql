-- Script para actualizar la columna image_url de varchar(255) a TEXT
-- Ejecutar este script en tu base de datos PostgreSQL

ALTER TABLE products 
ALTER COLUMN image_url TYPE TEXT;

-- Verificar el cambio
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'products' AND column_name = 'image_url'; 