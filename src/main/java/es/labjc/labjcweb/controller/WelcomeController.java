package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.service.SiteStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <-- OJO: Es @Controller, no @RestController
public class WelcomeController {

    @Autowired
    private SiteStatsService statsService;

    @GetMapping("/") // <-- Responde a la URL raíz: http://labjc.es/
    public String welcome(Model model) {

        // 1. Incrementa el contador y obtiene el nuevo valor
        long visitCount = statsService.incrementVisitCount();

        // 2. Pasa el valor a la plantilla HTML (para que Thymeleaf lo use)
        model.addAttribute("totalVisits", visitCount);

        // 3. Devuelve el nombre del archivo HTML
        // (Spring + Thymeleaf buscarán: /resources/templates/index.html)
        return "index";
    }
}