package es.labjc.labjcweb;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import es.labjc.labjcweb.model.DownloadableApp;
import es.labjc.labjcweb.repository.DownloadableAppRepository;
import es.labjc.labjcweb.service.DownloadService;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("DownloadController – integración MockMvc")
class DownloadControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    // Mockeamos los dos beans que usa el controlador
    @MockBean
    private DownloadableAppRepository appRepository;

    @MockBean
    private DownloadService downloadService;

    // ── /descargas ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /descargas devuelve HTTP 200 y la vista correcta")
    void getDescargas_status200() throws Exception {
        DownloadableApp app = new DownloadableApp();
        app.setId(1L);
        app.setAppName("Test SCS App");
        app.setDescription("App de test para oposiciones SCS");
        app.setFileName("test-scs-v1.apk");
        app.setDownloadCount(10L);

        when(appRepository.findAll()).thenReturn(List.of(app));
        when(downloadService.getTotalDownloadCount()).thenReturn(10L);

        mockMvc.perform(get("/descargas"))
                .andExpect(status().isOk())
                .andExpect(view().name("descargas"))
                .andExpect(model().attributeExists("apps"))
                .andExpect(model().attributeExists("totalDownloads"));
    }

    @Test
    @DisplayName("GET /descargas con lista vacía no da error")
    void getDescargas_listaVacia_sinError() throws Exception {
        when(appRepository.findAll()).thenReturn(List.of());
        when(downloadService.getTotalDownloadCount()).thenReturn(0L);

        mockMvc.perform(get("/descargas"))
                .andExpect(status().isOk())
                .andExpect(view().name("descargas"));
    }

    // ── /descargar/{id} ───────────────────────────────────────────────

    @Test
    @DisplayName("GET /descargar/{id} con ID inexistente lanza excepción")
    void descargar_idInexistente_lanzaExcepcion() {
        // El controlador no tiene @ExceptionHandler, así que la excepción
        // se propaga. Verificamos que efectivamente se lanza.
        when(downloadService.incrementAndGetApp(anyLong()))
                .thenThrow(new IllegalArgumentException("App no encontrada con ID: 9999"));

        assertThrows(Exception.class, () -> mockMvc.perform(get("/descargar/9999")));
    }

    @Test
    @DisplayName("GET /descargar/{id} con fileName con '..' devuelve 400")
    void descargar_pathTraversalEnBD_400() throws Exception {
        // Simulamos un registro en BD con fileName malicioso
        DownloadableApp appMaliciosa = new DownloadableApp();
        appMaliciosa.setId(2L);
        appMaliciosa.setFileName("../../etc/passwd");

        when(downloadService.incrementAndGetApp(2L))
                .thenReturn(appMaliciosa);

        mockMvc.perform(get("/descargar/2"))
                .andExpect(status().isBadRequest()); // HTTP 400
    }
}