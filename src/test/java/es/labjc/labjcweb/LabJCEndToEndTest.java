package es.labjc.labjcweb;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("LabJC E2E – flujos de usuario con Selenium")
class LabJCEndToEndTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void setUpDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1280,800");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ── FLUJO 1: Página de inicio ─────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("E2E-01 La página de inicio carga y muestra la navbar")
    void paginaInicio_cargaCorrectamente() {
        driver.get(url("/"));

        // Esperamos a que el body esté presente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Verificar que la navbar existe
        assertTrue(driver.findElements(By.cssSelector("nav")).size() > 0,
                "Debe existir una barra de navegación");

        // Verificar que hay contenido en la página
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertFalse(bodyText.isBlank(), "La página de inicio no debe estar vacía");
    }

    // ── FLUJO 2: Navegación al blog ───────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("E2E-02 Navegar al blog desde la navbar")
    void navegacionAlBlog_desdeNavbar() {
        driver.get(url("/"));

        // Buscar enlace al blog
        WebElement blogLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Blog")));
        blogLink.click();

        // Verificar que estamos en /blog
        wait.until(ExpectedConditions.urlContains("/blog"));
        assertTrue(driver.getCurrentUrl().contains("/blog"));

        // No debe haber error 500
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertFalse(bodyText.contains("Error 500"),
                "No debe mostrarse error 500 en el blog");
    }

    // ── FLUJO 3: Sección de descargas ─────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("E2E-03 La sección /descargas carga sin errores")
    void seccionDescargas_cargaSinErrores() {
        driver.get(url("/descargas"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // No debe haber error 500
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertFalse(bodyText.contains("Error 500"),
                "No debe producirse error 500 en descargas");

        // La URL debe ser /descargas
        assertTrue(driver.getCurrentUrl().contains("/descargas"));
    }

    // ── FLUJO 4: Formulario de contacto ──────────────────────────────

    @Test
    @Order(4)
    @DisplayName("E2E-04 El formulario de contacto tiene los campos esperados")
    void formularioContacto_tieneLosCampos() {
        driver.get(url("/contacto"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));

        // Verificar que existen campos de entrada
        assertFalse(driver.findElements(By.cssSelector("input, textarea")).isEmpty(),
                "El formulario debe tener campos de entrada");
    }

    @Test
    @Order(5)
    @DisplayName("E2E-05 Enviar el formulario de contacto redirige a confirmación")
    void formularioContacto_envioValido_redirige() {
        driver.get(url("/contacto"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));

        // Rellenar nombre
        WebElement nombre = driver.findElement(
                By.cssSelector("input[name='senderName']"));
        nombre.sendKeys("Tester Automático");

        // Rellenar email
        WebElement email = driver.findElement(
                By.cssSelector("input[name='senderEmail']"));
        email.sendKeys("test@labjc.es");

        // Rellenar mensaje
        WebElement mensaje = driver.findElement(
                By.cssSelector("textarea[name='message']"));
        mensaje.sendKeys("Mensaje enviado automáticamente por Selenium.");

        // Scroll hasta el botón para asegurarnos de que está visible
        WebElement submit = driver.findElement(
                By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", submit);

        // Clic via JavaScript para evitar que la navbar lo intercepte
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", submit);

        // Esperar redirección a /contacto-exito
        wait.until(ExpectedConditions.urlContains("/contacto-exito"));
        assertTrue(driver.getCurrentUrl().contains("contacto-exito"),
                "Debe redirigir a la página de confirmación");
    }
}