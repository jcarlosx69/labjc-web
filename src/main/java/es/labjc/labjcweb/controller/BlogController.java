package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.model.BlogPost;
import es.labjc.labjcweb.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @GetMapping("/blog")
    public String blogList(Model model) {
        // 1. Busca TODOS los posts en la BBDD
        List<BlogPost> posts = blogPostRepository.findAll();

        // 2. Los pasa a la plantilla
        model.addAttribute("posts", posts);

        // 3. Devuelve el HTML (buscará /resources/templates/blog.html)
        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String blogPost(@PathVariable Long id, Model model) {
        // 1. Busca el post por su ID. Si no lo encuentra, devuelve un post "vacío" o error.
        BlogPost post = blogPostRepository.findById(id)
                .orElse(null); // Aquí podríamos redirigir a un error 404

        // 2. Lo pasa a la plantilla
        model.addAttribute("post", post);

        // 3. Devuelve el HTML (buscará /resources/templates/post-detail.html)
        return "post-detail";
    }
}