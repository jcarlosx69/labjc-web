package es.labjc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder enc) {
        var adminUser = System.getProperty("APP_ADMIN_USER", "carlos");
        var adminPass = System.getProperty("APP_ADMIN_PASS", "661660");

        var admin = User.withUsername(adminUser)
                .password(enc.encode(adminPass))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Permite H2 Console y excepciones de CSRF en rutas concretas
                .csrf(csrf -> csrf
                        // 1) Ignorar CSRF para H2 Console (RequestMatcher)
                        .ignoringRequestMatchers(PathRequest.toH2Console())
                        // 2) Ignorar CSRF para endpoints concretos (String)
                        .ignoringRequestMatchers("/contacto/enviar", "/descargas/**"))

                .headers(h -> h
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; img-src 'self' data:; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'"))
                        .frameOptions(frame -> frame.sameOrigin()) // necesario para H2 console
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/blog/**", "/proyectos/**", "/descargas/**",
                                "/contacto/**", "/css/**", "/img/**", "/js/**")
                        .permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/admin/**", "/actuator/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        //.loginPage("/login").permitAll())
                        .defaultSuccessUrl("/admin", true))
                .logout(Customizer.withDefaults());

        return http.build();
    }
}
