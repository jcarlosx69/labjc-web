package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.repository.DownloadLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    @Autowired
    private DownloadLogRepository downloadLogRepository;

    /**
     * Devuelve el historial de descargas agrupadas por día y app.
     * Ruta: GET /api/admin/stats/downloads/daily
     * Requiere token JWT (protegido por SecurityConfig).
     */
    @GetMapping("/downloads/daily")
    public List<Map<String, Object>> getDailyStats() {

        return downloadLogRepository.findDailyDownloadStats()
                .stream()
                .map(row -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("app",   row[0]);  // appName
                    entry.put("fecha", row[1]);  // fecha (LocalDate)
                    entry.put("total", row[2]);  // COUNT
                    return entry;
                })
                .toList();
    }

    /**
     * Devuelve el total histórico de descargas por app.
     * Ruta: GET /api/admin/stats/downloads/total
     * Requiere token JWT (protegido por SecurityConfig).
     */
    @GetMapping("/downloads/total")
    public List<Map<String, Object>> getTotalStats() {

        return downloadLogRepository.findTotalDownloadsByApp()
                .stream()
                .map(row -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("app",   row[0]);  // appName
                    entry.put("total", row[1]);  // COUNT
                    return entry;
                })
                .toList();
    }
}