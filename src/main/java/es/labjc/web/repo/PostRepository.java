package es.labjc.web.repo;

import es.labjc.web.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Devuelve los posts publicados, ordenados de más reciente a más antiguo
    List<Post> findByPublicadoTrueOrderByFechaPublicacionDesc();

    // Devuelve un post concreto según el slug
    Optional<Post> findBySlug(String slug);

    List<Post> findTop5ByPublicadoTrueOrderByFechaPublicacionDesc();
}
