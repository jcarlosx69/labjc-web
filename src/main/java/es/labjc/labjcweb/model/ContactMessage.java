package es.labjc.labjcweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderName; // Nombre de quien envía
    private String senderEmail; // Email de quien envía
    private String message; // El mensaje
    private LocalDateTime sentAt; // Fecha de envío

    @Column(name ="is_read")
    private boolean read = false; // Para marcar si ya lo has leído o no

    // Constructor útil
    public ContactMessage(String senderName, String senderEmail, String message) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }
}