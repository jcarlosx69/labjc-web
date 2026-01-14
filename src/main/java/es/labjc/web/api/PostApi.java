package es.labjc.web.api;

import es.labjc.web.domain.Post;
import es.labjc.web.repo.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostApi {

    private final PostRepository posts;

    public PostApi(PostRepository posts) {
        this.posts = posts;
    }

    @GetMapping("/publicados")
    public List<Post> publicados() {
        return posts.findByPublicadoTrueOrderByFechaPublicacionDesc();
    }

    @GetMapping("/ultimos")
    public List<Post> ultimos() {
        return posts.findTop5ByPublicadoTrueOrderByFechaPublicacionDesc();
    }
}
