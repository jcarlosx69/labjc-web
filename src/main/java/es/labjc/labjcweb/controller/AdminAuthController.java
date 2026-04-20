package es.labjc.labjcweb.controller;

import es.labjc.labjcweb.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final JwtUtil jwtUtil;

    // Credenciales inyectadas desde properties
    @Value("${labjc.admin.username}")
    private String adminUsername;

    @Value("${labjc.admin.password}")
    private String adminPassword;

    public AdminAuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Endpoint de login del panel admin.
     * Recibe usuario y contraseña en el body (JSON).
     * Si son correctas devuelve un token JWT.
     * Si no, devuelve 401 Unauthorized.
     *
     * POST /api/admin/auth/login
     * Body: { "username": "admin", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        // Validamos usuario y contraseña contra los valores de properties
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            String token = jwtUtil.generateToken(username);
            // Devolvemos el token al cliente
            return ResponseEntity.ok(Map.of("token", token));
        }

        // Credenciales incorrectas
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
    }
}