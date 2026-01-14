package es.labjc.web.service;

import es.labjc.web.repo.VisitaRepo;
import es.labjc.web.domain.Visita;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContadorService {

    private final VisitaRepo visitaRepo;

    public ContadorService(VisitaRepo visitaRepo) {
        this.visitaRepo = visitaRepo;
    }

    /* === Registro de visitas === */
    @Transactional
    public void registrarVisita(String ruta) {
        if (ruta == null || ruta.isBlank()) return;
        if (ruta.length() > 255) ruta = ruta.substring(0, 255);
        visitaRepo.save(new Visita(ruta));
    }

    /* === Series y tops para el panel === */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> visitasUltimosDias(int dias) {
        LocalDateTime desde = LocalDate.now().minusDays(dias - 1L).atStartOfDay();
        var filas = visitaRepo.conteoPorDia(desde);
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] f : filas) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("dia", String.valueOf(f[0]));
            m.put("total", ((Number) f[1]).longValue());
            lista.add(m);
        }
        return lista;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> topRutas(int dias, int limite) {
        LocalDateTime desde = LocalDate.now().minusDays(dias - 1L).atStartOfDay();
        var filas = visitaRepo.topRutas(desde, limite);
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] f : filas) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("ruta", (String) f[0]);
            m.put("total", ((Number) f[1]).longValue());
            lista.add(m);
        }
        return lista;
    }

    /* === Totales === */
    @Transactional(readOnly = true)
    public long totalVisitas() {
        return visitaRepo.count();
    }

    @Transactional(readOnly = true)
    public long visitasHoy() {
        var ahora = LocalDateTime.now();
        var inicioHoy = ahora.toLocalDate().atStartOfDay();
        return visitaRepo.countByFechaBetween(inicioHoy, ahora);
    }

    @Transactional(readOnly = true)
    public long visitasUltimosDiasTotal(int dias) {
        LocalDateTime desde = LocalDate.now().minusDays(dias - 1L).atStartOfDay();
        return visitaRepo.countByFechaBetween(desde, LocalDateTime.now());
    }
}
