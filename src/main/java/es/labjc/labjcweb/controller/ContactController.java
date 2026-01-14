package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.model.ContactMessage;
import es.labjc.labjcweb.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageRepository contactRepository;

    // 1. Muestra la página con el formulario
    @GetMapping("/contacto")
    public String showContactForm(Model model) {
        // Crea un objeto vacío para que Thymeleaf pueda rellenarlo
        model.addAttribute("contactMessage", new ContactMessage());
        return "contacto"; // Devuelve /resources/templates/contacto.html
    }

    // 2. Recibe los datos del formulario cuando el usuario lo envía
    @PostMapping("/contacto")
    public String submitContactForm(
            @ModelAttribute("contactMessage") ContactMessage contactMessage) {

        // Establece la fecha de envío
        contactMessage.setSentAt(LocalDateTime.now());
        contactMessage.setRead(false);          // por si acaso

        // Guarda el mensaje en la BBDD
        contactRepository.save(contactMessage);

        // Redirige al usuario a una página de "gracias"
        return "redirect:/contacto-exito";
    }


    // 3. Página de "Gracias"
    @GetMapping("/contacto-exito")
    public String contactSuccess() {
        return "contacto-exito"; // Devuelve /resources/templates/contacto-exito.html
    }
}