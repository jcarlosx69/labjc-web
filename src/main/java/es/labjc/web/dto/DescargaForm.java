package es.labjc.web.dto;

import jakarta.validation.constraints.*;

public class DescargaForm {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "Máximo 200 caracteres")
    private String nombre;

    @NotBlank(message = "La ruta del archivo es obligatoria")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String rutaArchivo;

    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "Debe ser SHA-256 válido de 64 hex", groups = ValidChecksum.class)
    private String checksumSha256;

    private boolean habilitado = true;

    public interface ValidChecksum {}

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
}
