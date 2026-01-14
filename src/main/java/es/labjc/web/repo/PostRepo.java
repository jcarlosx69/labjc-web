package es.labjc.web.repo;

import es.labjc.web.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {

    Optional<Post> findBySlugAndPublicadoTrue(String slug);

    boolean existsBySlug(String slug);

    @Query("SELECT p FROM Post p WHERE " +
           "(:q IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%',:q,'%'))) " +
           "ORDER BY p.fechaPublicacion DESC")
    List<Post> buscarOrdenados(String q);

    @Query("SELECT p FROM Post p WHERE p.publicado = true " +
           "ORDER BY p.fechaPublicacion DESC")
    List<Post> publicadosRecientes();
}
