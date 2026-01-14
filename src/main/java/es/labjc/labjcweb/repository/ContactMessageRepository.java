package es.labjc.labjcweb.repository;

import es.labjc.labjcweb.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    // Spring Data JPA es tan inteligente que si defines un método
    // con un nombre específico, él sabe cómo implementarlo.
    // Por ejemplo, para buscar mensajes no leídos:
    // List<ContactMessage> findByReadFalse();
}