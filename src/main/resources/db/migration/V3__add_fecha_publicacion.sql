-- Añadir fecha_publicacion si no existe
ALTER TABLE posts
  ADD COLUMN IF NOT EXISTS fecha_publicacion TIMESTAMP NULL;

-- Índice útil para listado por publicados y fecha de publicación
CREATE INDEX IF NOT EXISTS idx_posts_publicado_fecha
  ON posts(publicado, fecha_publicacion);
