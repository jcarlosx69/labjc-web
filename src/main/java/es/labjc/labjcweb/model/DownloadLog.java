package es.labjc.labjcweb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "download_log")
public class DownloadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la app descargada (FK: app_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private DownloadableApp app;

    // Fecha y hora exacta de la descarga
    @Column(name = "downloaded_at", nullable = false)
    private LocalDateTime downloadedAt;

    // IP del usuario (opcional, puede ser null)
    @Column(name = "ip_address")
    private String ipAddress;

    // Constructor vacío requerido por JPA
    public DownloadLog() {}

    // Constructor de conveniencia para crear registros fácilmente
    public DownloadLog(DownloadableApp app, LocalDateTime downloadedAt, String ipAddress) {
        this.app = app;
        this.downloadedAt = downloadedAt;
        this.ipAddress = ipAddress;
    }

    // Getters
    public Long getId() { return id; }
    public DownloadableApp getApp() { return app; }
    public LocalDateTime getDownloadedAt() { return downloadedAt; }
    public String getIpAddress() { return ipAddress; }

    // Setters
    public void setApp(DownloadableApp app) { this.app = app; }
    public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}