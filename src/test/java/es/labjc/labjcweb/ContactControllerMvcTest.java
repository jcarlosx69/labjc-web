package es.labjc.labjcweb;

import es.labjc.labjcweb.repository.ContactMessageRepository;
import es.labjc.labjcweb.model.ContactMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ContactController – integración MockMvc")
class ContactControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactMessageRepository contactRepository;

    // ── GET /contacto ─────────────────────────────────────────────────

    @Test
    @DisplayName("GET /contacto devuelve HTTP 200 y el formulario")
    void getContacto_status200() throws Exception {
        mockMvc.perform(get("/contacto"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacto"))
                .andExpect(model().attributeExists("contactMessage"));
    }

    // ── GET /contacto-exito ───────────────────────────────────────────

    @Test
    @DisplayName("GET /contacto-exito devuelve HTTP 200")
    void getContactoExito_status200() throws Exception {
        mockMvc.perform(get("/contacto-exito"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacto-exito"));
    }

    // ── POST /contacto ────────────────────────────────────────────────

    @Test
    @DisplayName("POST /contacto con datos válidos redirige a confirmación")
    void postContacto_datosValidos_redirige() throws Exception {
        // Simulamos que el repositorio guarda sin problemas
        when(contactRepository.save(any(ContactMessage.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/contacto")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("senderName", "Juan Test")
                        .param("senderEmail", "juan@test.com")
                        .param("message", "Mensaje de prueba desde los tests automatizados"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacto-exito"));

        // Verificamos que se llamó a save() exactamente una vez
        verify(contactRepository, times(1)).save(any(ContactMessage.class));
    }

    @Test
    @DisplayName("POST /contacto cuando el repositorio falla lanza excepción")
    void postContacto_repositorioFalla_lanzaExcepcion() {
        // Simulamos que la BD está caída
        when(contactRepository.save(any(ContactMessage.class)))
                .thenThrow(new RuntimeException("Fallo simulado de BD"));

        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/contacto")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("senderName", "Juan Test")
                        .param("senderEmail", "juan@test.com")
                        .param("message", "Mensaje cuando la BD está caída"))
        );
    }
}