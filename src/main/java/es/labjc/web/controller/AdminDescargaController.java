package es.labjc.web.controller;

import es.labjc.web.domain.Descarga;
import es.labjc.web.dto.DescargaForm;
import es.labjc.web.repo.DescargaRepo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Controller
@RequestMapping("/admin/descargas")
public class AdminDescargaController {

    private final DescargaRepo repo;

    public AdminDescargaController(DescargaRepo repo) {
        this.repo = repo;
    }

    /* LISTAR */
    @GetMapping
    public String listar(Model model,
                         @RequestParam(value="q", required=false) String q) {
        var todas = (q == null || q.isBlank())
                ? repo.findAll()
                : repo.findAll().stream()
                    .filter(d -> d.getNombre().toLowerCase().contains(q.toLowerCase()))
                    .toList();
        model.addAttribute("lista", todas);
        model.addAttribute("q", q);
        model.addAttribute("title", "Descargas");
        return "admin/descargas/lista";
    }

    /* CREAR (form) */
    @GetMapping("/new")
    public String nueva(Model model) {
        model.addAttribute("form", new DescargaForm());
        model.addAttribute("title", "Nueva descarga");
        return "admin/descargas/form";
    }

    /* EDITAR (form) */
    @GetMapping("/{id}/edit")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Descarga> opt = repo.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error","Descarga no encontrada");
            return "redirect:/admin/descargas";
        }
        var d = opt.get();
        var f = new DescargaForm();
        f.setId(d.getId());
        f.setNombre(d.getNombre());
        f.setRutaArchivo(d.getRutaArchivo());
        f.setChecksumSha256(d.getChecksumSha256());
        f.setHabilitado(d.isHabilitado());
        model.addAttribute("form", f);
        model.addAttribute("title", "Editar descarga");
        return "admin/descargas/form";
    }

    /* GUARDAR (create/update) */
    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("form") DescargaForm form,
                          BindingResult br,
                          RedirectAttributes ra,
                          Model model) {

        // Validación extra: comprobar existencia de fichero (informativo)
        try {
            if (!Files.exists(Path.of(form.getRutaArchivo()))) {
                br.rejectValue("rutaArchivo", "ruta.noexiste", "El archivo no existe en esa ruta");
            }
        } catch (Exception ignored) {}

        if (br.hasErrors()) {
            model.addAttribute("title", form.getId() == null ? "Nueva descarga" : "Editar descarga");
            return "admin/descargas/form";
        }

        Descarga d = (form.getId() == null)
                ? new Descarga()
                : repo.findById(form.getId()).orElse(new Descarga());

        d.setNombre(form.getNombre());
        d.setRutaArchivo(form.getRutaArchivo());
        d.setChecksumSha256((form.getChecksumSha256()==null || form.getChecksumSha256().isBlank()) ? null : form.getChecksumSha256());
        d.setHabilitado(form.isHabilitado());

        repo.save(d);
        ra.addFlashAttribute("ok", "Descarga guardada correctamente");
        return "redirect:/admin/descargas";
    }

    /* BORRAR */
    @PostMapping("/{id}/delete")
    public String borrar(@PathVariable Long id, RedirectAttributes ra) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            ra.addFlashAttribute("ok", "Descarga eliminada");
        } else {
            ra.addFlashAttribute("error", "No existe la descarga");
        }
        return "redirect:/admin/descargas";
    }
}
