package es.labjc.labjcweb;

import es.labjc.labjcweb.model.SiteStats;
import es.labjc.labjcweb.repository.SiteStatsRepository;
import es.labjc.labjcweb.service.SiteStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SiteStatsService – contador de visitas (unit + mock)")
class SiteStatsServiceTest {

    @Mock
    private SiteStatsRepository statsRepository; // simulado, no toca BD real

    @InjectMocks
    private SiteStatsService siteStatsService;   // clase que estamos probando

    private SiteStats statsExistente;

    @BeforeEach
    void setUp() {
        // Preparamos un objeto SiteStats con 42 visitas
        // para usarlo en los tests
        statsExistente = new SiteStats();
        statsExistente.setTotalVisits(42L);
    }

    // ── Casos POSITIVOS ──────────────────────────────────────────────

    @Test
    @DisplayName("incrementVisitCount incrementa el contador en 1")
    void incrementVisitCount_incrementaEnUno() {
        // Simulamos que el repositorio encuentra el registro con ID=1
        when(statsRepository.findById(1L))
                .thenReturn(Optional.of(statsExistente));
        when(statsRepository.save(any(SiteStats.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        long resultado = siteStatsService.incrementVisitCount();

        // El resultado debe ser 43 (42 + 1)
        assertEquals(43L, resultado,
                "El contador debe pasar de 42 a 43");

        // Verificamos que se llamó a save() exactamente una vez
        verify(statsRepository, times(1)).save(statsExistente);
    }

    @Test
    @DisplayName("getVisitCount devuelve el valor actual")
    void getVisitCount_devuelveValorActual() {
        when(statsRepository.findById(1L))
                .thenReturn(Optional.of(statsExistente));

        long count = siteStatsService.getVisitCount();

        assertEquals(42L, count);
    }

    // ── Casos NEGATIVOS ──────────────────────────────────────────────

    @Test
    @DisplayName("incrementVisitCount sin registro previo arranca desde 1")
    void incrementVisitCount_sinRegistro_arrancaDesdeUno() {
        // Simulamos BD vacía: findById devuelve Optional.empty()
        // El servicio hará .orElse(new SiteStats()) → totalVisits=0
        when(statsRepository.findById(1L))
                .thenReturn(Optional.empty());
        when(statsRepository.save(any(SiteStats.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        long resultado = siteStatsService.incrementVisitCount();

        assertEquals(1L, resultado,
                "Si no hay registro previo, la primera visita debe ser 1");
    }

    @Test
    @DisplayName("getVisitCount sin registro devuelve 0")
    void getVisitCount_sinRegistro_devuelveCero() {
        when(statsRepository.findById(1L))
                .thenReturn(Optional.empty());

        long count = siteStatsService.getVisitCount();

        assertEquals(0L, count);
    }
}