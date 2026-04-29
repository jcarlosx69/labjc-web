package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import es.labjc.labjcweb.service.DownloadService;
import jakarta.servlet.http.HttpServletRequest;
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
    @Value("${labjc.downloads.path}")
    private String downloadsPath;

    // 1. Muestra la página HTML con la lista de descargas
    @GetMapping("/descargas")
    public String showDownloadsPage(Model model) {

        List<DownloadableApp> apps = appRepository.findAll();
        long totalDownloads = downloadService.getTotalDownloadCount();

        model.addAttribute("apps", apps);
        model.addAttribute("totalDownloads", totalDownloads);

        return "descargas";
    }

    // 2. Maneja la descarga real, el contador y el registro del log
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id,
                                                  HttpServletRequest request) throws IOException {

        // Obtiene la IP del cliente para registrarla en el log
        String ipAddress = request.getRemoteAddr();

        // Incrementa contador, registra log y obtiene la app
        DownloadableApp app = downloadService.incrementAndGetApp(id, ipAddress);

        String fileName = app.getFileName();

        // Validación básica del nombre de archivo (evita rutas tipo ../ o separadores)
        if (fileName == null || fileName.isBlank()
                || fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }

        // Prepara el archivo para ser descargado desde carpeta externa
        Path filePath = Paths.get(downloadsPath, fileName);
        Resource resource = new PathResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Prepara la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // Devuelve la respuesta con el archivo
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}