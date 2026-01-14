package es.labjc.web.config;

import es.labjc.web.service.ContadorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ContadorService contadorService;

    public WebConfig(ContadorService contadorService) {
        this.contadorService = contadorService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContadorInterceptor(contadorService));
    }
}
