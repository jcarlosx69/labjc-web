package es.labjc.labjcweb.repository;

import es.labjc.labjcweb.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Buena práctica, aunque no estrictamente necesario
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    // JpaRepository<TipoDeEntidad, TipoDelId>

    // Con esto ya tenemos:
    // .save(post) -> Guardar o actualizar un post
    // .findById(id) -> Buscar un post por su ID
    // .findAll() -> Obtener todos los posts
    // .deleteById(id) -> Borrar un post
}