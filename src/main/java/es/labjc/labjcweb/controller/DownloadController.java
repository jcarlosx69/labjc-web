package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import es.labjc.labjcweb.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    private DownloadableAppRepository appRepository;

    @Autowired
    private DownloadService downloadService;

    // Ruta externa configurable donde se guardan los archivos descargables
    // Ejemplo: /opt/labjc-web/downloads
    @Value("${labjc.downloads.path}")
    private String downloadsPath;

    // 1. Muestra la página HTML con la lista de descargas
    @GetMapping("/descargas")
    public String showDownloadsPage(Model model) {

        // Obtenemos todas las apps
        List<DownloadableApp> apps = appRepository.findAll();

        // Obtenemos el contador total desde nuestro servicio
        long totalDownloads = downloadService.getTotalDownloadCount();

        // Las pasamos al modelo
        model.addAttribute("apps", apps);
        model.addAttribute("totalDownloads", totalDownloads);

        return "descargas"; // Devuelve /resources/templates/descargas.html
    }

    // 2. Maneja la descarga real y el contador
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {

        // 1. Llama al servicio para incrementar el contador y obtener la app
        DownloadableApp app = downloadService.incrementAndGetApp(id);

        String fileName = app.getFileName();

        // Validación básica del nombre de archivo (evita rutas tipo ../ o separadores)
        if (fileName == null || fileName.isBlank()
                || fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Prepara el archivo para ser descargado desde carpeta externa
        Path filePath = Paths.get(downloadsPath, fileName);
        Resource resource = new PathResource(filePath);

        if (!resource.exists()) {
            // Manejo de error si el archivo no existe en el servidor
            return ResponseEntity.notFound().build();
        }

        // 3. Prepara la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        // Esto le dice al navegador que debe "descargar" el archivo
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // 4. Devuelve la respuesta (el archivo)
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Tipo genérico para descarga
                .body(resource);
    }
}