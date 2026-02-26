package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import es.labjc.labjcweb.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    private DownloadableAppRepository appRepository;

    @Autowired
    private DownloadService downloadService;

    // Ruta donde se guardarán los archivos (dentro del proyecto)
    // EJ: src/main/resources/static/files/tu_app.zip
    private static final String FILE_DIRECTORY = "static/files/";


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

        // 2. Prepara el archivo para ser descargado
        // Buscamos el archivo en la carpeta /resources/static/files/
        Resource resource = new ClassPathResource(FILE_DIRECTORY + app.getFileName());

        if (!resource.exists()) {
            // Manejo de error si el archivo no existe en el servidor
            return ResponseEntity.notFound().build();
        }

        // 3. Prepara la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        // Esto le dice al navegador que debe "descargar" el archivo
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + app.getFileName());

        // 4. Devuelve la respuesta (el archivo)
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Tipo genérico para descarga
                .body(resource);
    }
}