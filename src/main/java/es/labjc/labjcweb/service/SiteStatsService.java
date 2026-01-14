package es.labjc.labjcweb.service;

import es.labjc.labjcweb.model.SiteStats;
import es.labjc.labjcweb.repository.SiteStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // <-- Le dice a Spring que esta clase contiene lógica de negocio
public class SiteStatsService {

    @Autowired // <-- Spring inyectará automáticamente el repositorio aquí
    private SiteStatsRepository statsRepository;

    // Usamos @Transactional para asegurar que la operación de leer y
    // guardar sea atómica (o todo funciona, o no se hace nada).
    @Transactional
    public long incrementVisitCount() {
        // ID=1L (el 'L' es por Long) es el ID fijo que definimos
        // para nuestra fila única de estadísticas.

        // 1. Busca las estadísticas. Si no existen (la primera vez), crea una nueva.
        SiteStats stats = statsRepository.findById(1L)
                .orElse(new SiteStats()); // .orElse() crea una nueva si no la encuentra

        // 2. Incrementa el contador
        stats.setTotalVisits(stats.getTotalVisits() + 1);

        // 3. Guarda los cambios en la BBDD
        statsRepository.save(stats);

        // 4. Devuelve el nuevo total
        return stats.getTotalVisits();
    }

    // Método simple para solo LEER el contador actual
    public long getVisitCount() {
        return statsRepository.findById(1L)
                .map(SiteStats::getTotalVisits) // Si la encuentra, devuelve las visitas
                .orElse(0L); // Si no, devuelve 0
    }
}