package es.labjc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        // Renderizará templates/index.html si existe; si no, 404.
        return "index";
    }
}
