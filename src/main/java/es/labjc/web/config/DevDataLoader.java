package es.labjc.web.config;

import es.labjc.web.domain.Post;
import es.labjc.web.repo.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
public class DevDataLoader implements CommandLineRunner {
    private final PostRepository posts;

    public DevDataLoader(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public void run(String... args) {
        if (posts.count() > 0)
            return;

        Post publicado = new Post();
        publicado.setSlug("primer-post");
        publicado.setTitulo("Hola LabJC");
        publicado.setResumen("Resumen de prueba (<= 280 chars).");
        publicado.setContenidoMd("# Hola\nEste es un post sembrado para dev.");
        publicado.setPublicado(true);
        publicado.setFechaPublicacion(LocalDateTime.now());

        Post borrador = new Post();
        borrador.setSlug("borrador-1");
        borrador.setTitulo("Borrador");
        borrador.setResumen("Borrador de ejemplo.");
        borrador.setContenidoMd("Contenido *markdown* del borrador.");
        borrador.setPublicado(false);
        // fechaPublicacion null en borradores

        Post publicado2 = new Post();
        publicado2.setSlug("segundo-post");
        publicado2.setTitulo("Segundo LabJC");
        publicado2.setResumen("Resumen de prueba (<= 280 chars).");
        publicado2.setContenidoMd("# Hola\nEste es un post sembrado para dev.");
        publicado2.setPublicado(true);
        publicado2.setFechaPublicacion(LocalDateTime.now());

        posts.save(publicado);
        posts.save(borrador);
        posts.save(publicado2);
    }
}
