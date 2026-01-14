package es.labjc.web.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {
    private Long id;

    @NotBlank @Size(max=160)
    private String titulo;

    @Size(max=280)
    private String resumen;

    @NotBlank
    private String contenidoMd;

    @Size(max=180)
    private String slug;  // opcional: si va vacío se genera

    private boolean publicado;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getContenidoMd() { return contenidoMd; }
    public void setContenidoMd(String contenidoMd) { this.contenidoMd = contenidoMd; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
}
