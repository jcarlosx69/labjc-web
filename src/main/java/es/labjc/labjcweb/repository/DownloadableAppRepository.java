package es.labjc.labjcweb.repository;

import es.labjc.labjcweb.model.DownloadableApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadableAppRepository extends JpaRepository<DownloadableApp, Long> {
    // Con esto es suficiente para buscar, guardar y actualizar apps.
}