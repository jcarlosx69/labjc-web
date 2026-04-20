package es.labjc.labjcweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos CSRF: la API REST usa JWT, no cookies de sesión
            .csrf(csrf -> csrf.disable())

            // Configuración CORS para permitir peticiones desde el panel React
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Sin sesiones HTTP: cada petición se autentica con el token JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // ✅ Rutas públicas de la web Thymeleaf — sin cambios
                .requestMatchers("/", "/blog/**", "/descargas/**",
                 "/descargar/**", "/contacto/**",
                 "/contacto-exito").permitAll()

                // ✅ Recursos estáticos (CSS, JS, imágenes)
                .requestMatchers("/css/**", "/js/**", "/images/**",
                                 "/webjars/**").permitAll()

                // ✅ Endpoint de login del panel admin — público para obtener token
                .requestMatchers("/api/admin/auth/**").permitAll()

                // ✅ Actuator: solo el health check es público
                .requestMatchers("/actuator/health").permitAll()

                // 🔒 Todo lo demás bajo /api/admin/** requiere JWT válido
                .requestMatchers("/api/admin/**").authenticated()

                // 🔒 Cualquier otra ruta no listada: también requiere autenticación
                .anyRequest().authenticated()
            )

            // Añadimos nuestro filtro JWT antes del filtro estándar de usuario/contraseña
            .addFilterBefore(jwtAuthenticationFilter,
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración CORS: permite peticiones desde el panel React local (dev)
     * y desde admin.labjc.es (producción).
     * Ajusta los orígenes según tu dominio final.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos: React en local y el subdominio admin en producción
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",     // React dev server
            "https://admin.labjc.es"    // Panel admin en producción
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}