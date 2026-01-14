-- V2: añade fecha_publicado e índices asociados (sin recrear la tabla)

-- 1) Columna nueva (NULL por defecto)
ALTER TABLE posts ADD COLUMN IF NOT EXISTS fecha_publicado TIMESTAMP NULL;

-- 2) Índices (nombre distintos para no chocar con V1)
CREATE INDEX IF NOT EXISTS idx_posts_publicado_fecha ON posts (publicado, fecha_publicado);
CREATE INDEX IF NOT EXISTS idx_posts_fecha             ON posts (fecha_publicado);
-- (slug ya es UNIQUE en V1; si quieres un índice normal adicional, sería redundante)

-- 3) (Opcional) Inicializa fecha_publicado con 'creado' donde proceda
-- UPDATE posts SET fecha_publicado = creado WHERE publicado = TRUE AND fecha_publicado IS NULL;
