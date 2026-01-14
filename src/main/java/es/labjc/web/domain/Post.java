package es.labjc.web.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_fecha", columnList = "fechaPublicacion DESC"),
        @Index(name = "idx_posts_publicado", columnList = "publicado")
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(length = 280)
    private String resumen;

    @Lob
    @Column(name = "contenido_md", nullable = false, columnDefinition = "TEXT")
    private String contenidoMd;

    @Column(nullable = false, unique = true, length = 180)
    private String slug;

    @Column(nullable = false)
    private boolean publicado = false;

    @Column(name = "fecha_publicacion", nullable = true) // o simplemente elimina 'nullable'
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    // getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getContenidoMd() {
        return contenidoMd;
    }

    public void setContenidoMd(String contenidoMd) {
        this.contenidoMd = contenidoMd;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
