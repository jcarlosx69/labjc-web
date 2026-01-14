// src/main/java/es/labjc/web/domain/Visita.java
package es.labjc.web.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String ruta;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    public Visita() {}

    public Visita(String ruta) {
        this.ruta = ruta;
    }

    public Long getId() { return id; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
