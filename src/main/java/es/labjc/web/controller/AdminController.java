package es.labjc.web.controller;

import es.labjc.web.service.ContadorService;
import es.labjc.web.service.DescargaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final ContadorService contadorService;
    private final DescargaService descargaService;

    public AdminController(ContadorService contadorService, DescargaService descargaService) {
        this.contadorService = contadorService;
        this.descargaService = descargaService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        var visitasHoy = contadorService.visitasHoy();
        var visitas7 = contadorService.visitasUltimosDiasTotal(7);
        var visitasTotales = contadorService.totalVisitas();

        var topRutas = contadorService.topRutas(7, 10);
        var visitasUlt7Serie = contadorService.visitasUltimosDias(7);

        var topDescargas = descargaService.top(10);
        var descargasAcumuladas = descargaService.totalDescargasAcumuladas();

        model.addAttribute("title", "Panel de administración");
        model.addAttribute("visitasHoy", visitasHoy);
        model.addAttribute("visitas7", visitas7);
        model.addAttribute("visitasTotales", visitasTotales);
        model.addAttribute("visitasUlt7Serie", visitasUlt7Serie);

        model.addAttribute("topRutas", topRutas);
        model.addAttribute("topDescargas", topDescargas);
        model.addAttribute("descargasAcumuladas", descargasAcumuladas);

        return "admin/dashboard";
    }
}
