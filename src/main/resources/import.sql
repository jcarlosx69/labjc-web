-- Inserta la fila ÚNICA para las estadísticas del sitio (ID=1)
-- Es vital para el contador de visitas.
INSERT INTO site_stats (id, total_visits) VALUES (1, 0);

-- Inserta un par de posts de prueba para el blog
INSERT INTO blog_post (title, content, created_at) VALUES
('¡Bienvenido a mi blog!', 'Este es el primer post de mi blog con Spring Boot y MariaDB. El contenido puede incluir <b>HTML</b>.', CURRENT_TIMESTAMP),
('Mi segundo post', 'Aquí hablaré sobre mis proyectos. El contador de visitas ya debería estar funcionando en la página principal.', CURRENT_TIMESTAMP);

-- Inserta un par de aplicaciones de prueba para la página de descargas
INSERT INTO downloadable_app (app_name, description, file_name, download_count) VALUES
('Mi App v1.0', 'Una herramienta útil creada por mí. (Archivo de prueba 1)', 'mi_app_v1.zip', 0),
('Utilidad de Consola', 'Otro proyecto en Java. (Archivo de prueba 2)', 'utilidad_consola.jar', 0);