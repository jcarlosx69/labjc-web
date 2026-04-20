package es.labjc.labjcweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta inyectada desde application.properties (mínimo 32 caracteres)
    @Value("${labjc.admin.jwt.secret}")
    private String jwtSecret;

    // Tiempo de expiración del token en ms (inyectado desde properties)
    @Value("${labjc.admin.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Construye la clave criptográfica a partir del secreto configurado.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para el usuario indicado.
     * @param username nombre del usuario admin
     * @return token JWT firmado
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token token JWT
     * @return username contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Valida que el token sea correcto y no haya expirado.
     * @param token token JWT
     * @return true si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token); // lanza excepción si es inválido o ha expirado
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parsea y devuelve los claims (payload) del token.
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}