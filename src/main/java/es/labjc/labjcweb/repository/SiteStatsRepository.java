package es.labjc.labjcweb.repository;

import es.labjc.labjcweb.model.SiteStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteStatsRepository extends JpaRepository<SiteStats, Long> {
    // Al extender JpaRepository, ya tenemos .findById(1L) y .save(stats)
    // que es todo lo que necesitamos para leer y actualizar el contador.
}