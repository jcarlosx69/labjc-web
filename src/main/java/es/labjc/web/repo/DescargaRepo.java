package es.labjc.web.repo;

import es.labjc.web.domain.Descarga;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescargaRepo extends JpaRepository<Descarga, Long> {

    @Query("SELECT d FROM Descarga d WHERE d.id = :id AND d.habilitado = true")
    Optional<Descarga> findHabilitada(@Param("id") Long id);

    @Query("SELECT d FROM Descarga d ORDER BY d.totalDescargas DESC")
    List<Descarga> topDescargas(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.totalDescargas), 0) FROM Descarga d")
    long sumaTotalDescargas();
}
