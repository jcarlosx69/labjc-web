package es.labjc.web.controller;

import es.labjc.web.service.PostService;
import es.labjc.web.web.dto.PostForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final PostService service;

    public AdminPostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String lista(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("title", "Posts");
        model.addAttribute("lista", service.listar(q));
        model.addAttribute("q", q);
        return "admin/posts/lista";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("title", "Nuevo post");
        model.addAttribute("form", new PostForm());
        return "admin/posts/form";
    }

    @GetMapping("/{id}/edit")
    public String editar(@PathVariable Long id, Model model) {
        var p = service.get(id);
        var f = new PostForm();
        f.setId(p.getId());
        f.setTitulo(p.getTitulo());
        f.setResumen(p.getResumen());
        f.setContenidoMd(p.getContenidoMd());
        f.setSlug(p.getSlug());
        f.setPublicado(p.isPublicado());
        model.addAttribute("title", "Editar post");
        model.addAttribute("form", f);
        return "admin/posts/form";
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("form") PostForm form,
                          BindingResult br,
                          RedirectAttributes ra,
                          Model model) {
        if (br.hasErrors()) {
            model.addAttribute("title", form.getId()==null ? "Nuevo post" : "Editar post");
            return "admin/posts/form";
        }
        var p = service.guardar(form);
        ra.addFlashAttribute("ok", "Post guardado correctamente.");
        return "redirect:/admin/posts";
    }

    @PostMapping("/{id}/delete")
    public String borrar(@PathVariable Long id, RedirectAttributes ra) {
        service.borrar(id);
        ra.addFlashAttribute("ok", "Post eliminado.");
        return "redirect:/admin/posts";
    }
}
