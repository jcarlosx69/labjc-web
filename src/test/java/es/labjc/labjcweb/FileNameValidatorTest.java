package es.labjc.labjcweb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validación de nombres de archivo (unit)")
class FileNameValidatorTest {

    private boolean isValidFileName(String fileName) {
        return !(fileName == null
                || fileName.isBlank()
                || fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\"));
    }

    @Test
    @DisplayName("Nombre válido: APK estándar")
    void nombreValido_apkNormal() {
        assertTrue(isValidFileName("test-scs-v1.apk"));
    }

    @Test
    @DisplayName("Nombre válido: archivo ZIP")
    void nombreValido_zip() {
        assertTrue(isValidFileName("herramienta-v2.0.zip"));
    }

    @Test
    @DisplayName("Nombre nulo debe ser rechazado")
    void nombreNulo_rechazado() {
        assertFalse(isValidFileName(null));
    }

    @Test
    @DisplayName("Cadena vacía debe ser rechazada")
    void nombreVacio_rechazado() {
        assertFalse(isValidFileName(""));
    }

    @Test
    @DisplayName("Cadena en blanco debe ser rechazada")
    void nombreBlanco_rechazado() {
        assertFalse(isValidFileName("   "));
    }

    @Test
    @DisplayName("Path traversal con '..' debe ser rechazado")
    void pathTraversal_doblePunto() {
        assertFalse(isValidFileName("../../etc/passwd"));
    }

    @Test
    @DisplayName("Separador Unix '/' debe ser rechazado")
    void separadorUnix_rechazado() {
        assertFalse(isValidFileName("carpeta/archivo.apk"));
    }

    @Test
    @DisplayName("Separador Windows '\\' debe ser rechazado")
    void separadorWindows_rechazado() {
        assertFalse(isValidFileName("carpeta\\archivo.apk"));
    }

    @Test
    @DisplayName("Ruta absoluta debe ser rechazada")
    void rutaAbsoluta_rechazada() {
        assertFalse(isValidFileName("/opt/labjc/secreto.apk"));
    }
}