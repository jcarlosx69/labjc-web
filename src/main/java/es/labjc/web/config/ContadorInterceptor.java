package es.labjc.web.config;

import es.labjc.web.service.ContadorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class ContadorInterceptor implements HandlerInterceptor {

    private final ContadorService contadorService;
    private static final Set<String> IGNORAR_PREFIX = Set.of(
        "/css/", "/img/", "/js/", "/h2", "/admin", "/actuator"
    );

    public ContadorInterceptor(ContadorService contadorService) {
        this.contadorService = contadorService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        for (String p : IGNORAR_PREFIX) {
            if (uri.startsWith(p)) return true; // no contar
        }
        contadorService.registrarVisita(uri);
        return true;
    }
}
