package es.labjc.web.controller;

import es.labjc.web.domain.Post;
import es.labjc.web.repo.PostRepository;
import es.labjc.web.service.MarkdownService;
import es.labjc.web.web.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final PostRepository posts;
    private final MarkdownService md;

    public BlogController(PostRepository posts, MarkdownService md) {
        this.posts = posts;
        this.md = md;
    }

    // Listado de posts publicados (últimos primero)
    @GetMapping
    public String listado(Model model) {
        List<Post> publicados = posts.findByPublicadoTrueOrderByFechaPublicacionDesc();
        model.addAttribute("titulo", "Blog");
        model.addAttribute("posts", publicados);
        return "blog/list"; // templates/blog/list.html
    }

    // Detalle por slug
    @GetMapping("/{slug}")
    public String detalle(@PathVariable String slug, Model model) {
        // sólo posts publicados
        Post post = posts.findBySlug(slug)
                .filter(Post::isPublicado)
                .orElseThrow(() -> new NotFoundException("Post no encontrado: " + slug));

        String html = md.toHtml(Objects.requireNonNullElse(post.getContenidoMd(), ""));
        model.addAttribute("titulo", post.getTitulo());
        model.addAttribute("post", post);
        model.addAttribute("html", html);
        return "blog/detalle";
    }
}
