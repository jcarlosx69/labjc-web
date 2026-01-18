package es.labjc.labjcweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class DownloadableApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appName; // Nombre de la app (ej. "MiUtilidad v1.0")
    private String description; // Descripción corta

    // Nombre real del archivo en el servidor (ej. "mi_utilidad.zip")
    private String fileName;

    // El contador de descargas para ESTA app
    private long downloadCount = 0;

    // Constructor útil
    public DownloadableApp(String appName, String description, String fileName) {
        this.appName = appName;
        this.description = description;
        this.fileName = fileName;
    }
}