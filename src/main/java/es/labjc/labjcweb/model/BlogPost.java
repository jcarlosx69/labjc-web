package es.labjc.labjcweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.Data; // Importante para @Data
import lombok.NoArgsConstructor;

@Data // <-- Lombok: Crea getters, setters, toString, etc. automáticamente
@NoArgsConstructor // <-- Lombok: Crea un constructor vacío (requerido por JPA)
@Entity // <-- JPA: Le dice a Spring que esto es una tabla de BBDD
public class BlogPost {

    @Id // <-- JPA: Marca esto como la Clave Primaria (ID único)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- JPA: Autoinmcremental
    private Long id;

    private String title; // Título del post

    @Column(length = 4000) // <-- JPA: Aumenta el tamaño de la columna (por defecto 255)
    private String content; // El contenido (HTML o Markdown) del post

    private LocalDateTime createdAt; // Fecha de creación

    // Constructor para crear posts fácilmente (Lombok se encarga del resto)
    public BlogPost(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}