package es.labjc.web.service;

import es.labjc.web.domain.Post;
import es.labjc.web.repo.PostRepo;
import es.labjc.web.web.dto.PostForm;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PostService {

    private final PostRepo repo;
    private final Parser mdParser = Parser.builder().build();
    private final HtmlRenderer mdRenderer = HtmlRenderer.builder().build();

    public PostService(PostRepo repo) {
        this.repo = repo;
    }

    /* == Helpers == */
    public String markdownToHtml(String md) {
        Node doc = mdParser.parse(md == null ? "" : md);
        return mdRenderer.render(doc);
    }

    private String toSlug(String texto) {
        if (texto == null) texto = "";
        String base = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) base = "post";
        String slug = base;
        int i = 2;
        while (repo.existsBySlug(slug)) {
            slug = base + "-" + i++;
        }
        return slug;
    }

    /* == Admin == */
    @Transactional(readOnly = true)
    public List<Post> listar(String q) {
        return repo.buscarOrdenados((q == null || q.isBlank()) ? null : q.trim());
    }

    @Transactional(readOnly = true)
    public Post get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Transactional
    public Post guardar(PostForm form) {
        Post p = (form.getId() != null) ? repo.findById(form.getId()).orElse(new Post()) : new Post();
        p.setTitulo(form.getTitulo().trim());
        p.setResumen(form.getResumen());
        p.setContenidoMd(form.getContenidoMd());
        p.setPublicado(form.isPublicado());

        String slug = (form.getSlug() != null && !form.getSlug().isBlank())
                ? form.getSlug().trim()
                : toSlug(p.getTitulo());
        if (p.getId() == null || !slug.equals(p.getSlug())) {
            // si cambia el slug, valida unicidad
            if (repo.existsBySlug(slug)) slug = toSlug(slug);
        }
        p.setSlug(slug);

        if (p.getId() == null) {
            p.setFechaPublicacion(LocalDateTime.now());
        }
        return repo.save(p);
    }

    @Transactional
    public void borrar(Long id) {
        repo.deleteById(id);
    }

    /* == Público == */
    @Transactional(readOnly = true)
    public List<Post> recientesPublicados() {
        return repo.publicadosRecientes();
    }

    @Transactional(readOnly = true)
    public Post publicadoPorSlug(String slug) {
        return repo.findBySlugAndPublicadoTrue(slug).orElseThrow();
    }
}
