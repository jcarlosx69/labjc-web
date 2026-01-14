package es.labjc.labjcweb.service;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DownloadService {

    @Autowired
    private DownloadableAppRepository appRepository;

    /**
     * Obtiene el contador total sumando todos los contadores individuales.
     */
    public long getTotalDownloadCount() {
        // Usa un Stream de Java para sumar todos los "downloadCount"
        return appRepository.findAll().stream()
                .mapToLong(DownloadableApp::getDownloadCount)
                .sum();
    }

    /**
     * Incrementa el contador de una app específica y devuelve el nombre del archivo.
     * Es @Transactional para asegurar que la actualización se guarde.
     */
    @Transactional
    public DownloadableApp incrementAndGetApp(Long id) {
        // Busca la app. Si no la encuentra, tira una excepción
        DownloadableApp app = appRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("App no encontrada con ID: " + id));

        // Incrementa el contador
        app.setDownloadCount(app.getDownloadCount() + 1);

        // Guarda la app actualizada en la BBDD
        appRepository.save(app);

        // Devuelve la entidad de la app completa
        return app;
    }
}