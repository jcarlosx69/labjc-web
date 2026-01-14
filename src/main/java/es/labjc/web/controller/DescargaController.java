package es.labjc.web.controller;

import es.labjc.web.service.DescargaService;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;

@Controller
@RequestMapping("/descargas")
public class DescargaController {

    private final DescargaService descargaService;

    public DescargaController(DescargaService descargaService) {
        this.descargaService = descargaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> descargar(@PathVariable Long id) throws Exception {
        var d = descargaService.getHabilitada(id);
        descargaService.incrementarContador(id);

        Path file = Paths.get(d.getRutaArchivo());
        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }
}
