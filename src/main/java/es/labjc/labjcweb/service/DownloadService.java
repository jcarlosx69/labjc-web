package es.labjc.labjcweb.service;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.model.DownloadLog;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import es.labjc.labjcweb.repository.DownloadLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DownloadService {

    @Autowired
    private DownloadableAppRepository appRepository;

    @Autowired
    private DownloadLogRepository downloadLogRepository;

    /**
     * Obtiene el contador total sumando todos los contadores individuales.
     */
    public long getTotalDownloadCount() {
        return appRepository.findAll().stream()
                .mapToLong(DownloadableApp::getDownloadCount)
                .sum();
    }

    /**
     * Incrementa el contador de una app específica, registra el log
     * de descarga con IP y timestamp, y devuelve la entidad de la app.
     */
    @Transactional
    public DownloadableApp incrementAndGetApp(Long id, String ipAddress) {

        // Busca la app. Si no existe, lanza excepción
        DownloadableApp app = appRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("App no encontrada con ID: " + id));

        // Incrementa el contador existente (comportamiento original sin cambios)
        app.setDownloadCount(app.getDownloadCount() + 1);
        appRepository.save(app);

        // Registra la descarga en el log con timestamp actual e IP del cliente
        DownloadLog log = new DownloadLog(app, LocalDateTime.now(), ipAddress);
        downloadLogRepository.save(log);

        return app;
    }
}