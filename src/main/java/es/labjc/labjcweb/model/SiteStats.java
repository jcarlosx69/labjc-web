package es.labjc.labjcweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SiteStats {

    // Usamos un ID fijo (1L) porque esta tabla SOLO tendrá una fila.
    // NO usamos @GeneratedValue
    @Id
    private Long id = 1L;

    // El contador de visitas de la página de Bienvenida
    private long totalVisits = 0;

}