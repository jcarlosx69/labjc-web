package es.labjc.web.repo;

import es.labjc.web.domain.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitaRepo extends JpaRepository<Visita, Long> {

    long countByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    @Query(value = """
        SELECT CAST(fecha AS DATE) AS dia, COUNT(*) AS total
        FROM visitas
        WHERE fecha >= :desde
        GROUP BY CAST(fecha AS DATE)
        ORDER BY dia DESC
        """, nativeQuery = true)
    List<Object[]> conteoPorDia(@Param("desde") LocalDateTime desde);

    @Query(value = """
        SELECT ruta, COUNT(*) AS total
        FROM visitas
        WHERE fecha >= :desde
        GROUP BY ruta
        ORDER BY total DESC
        LIMIT :limite
        """, nativeQuery = true)
    List<Object[]> topRutas(@Param("desde") LocalDateTime desde,
                            @Param("limite") int limite);
}
