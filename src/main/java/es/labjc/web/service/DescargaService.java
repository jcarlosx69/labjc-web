package es.labjc.web.service;

import es.labjc.web.domain.Descarga;
import es.labjc.web.repo.DescargaRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DescargaService {
    private final DescargaRepo repo;

    public DescargaService(DescargaRepo repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public Descarga getHabilitada(Long id) {
        return repo.findHabilitada(id).orElseThrow(() -> new IllegalArgumentException("Descarga no disponible"));
    }

    @Transactional
    public void incrementarContador(Long id) {
        var d = getHabilitada(id);
        d.setTotalDescargas(d.getTotalDescargas() + 1);
        repo.save(d);
    }

    @Transactional(readOnly = true)
    public List<Descarga> top(int limite) {
        return repo.topDescargas(PageRequest.of(0, limite));
    }
	
	    @Transactional(readOnly = true)
    public long totalDescargasAcumuladas() {
        return repo.sumaTotalDescargas();
    }

}
