package es.labjc.web.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "descargas")
public class Descarga {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String nombre;

    @Column(name="ruta_archivo", nullable=false, length=255)
    private String rutaArchivo;

    @Column(name="checksum_sha256", length=64)
    private String checksumSha256;

    @Column(nullable=false)
    private boolean habilitado = true;

    @Column(name="total_descargas", nullable=false)
    private long totalDescargas = 0;

    // getters/setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
    public long getTotalDescargas() { return totalDescargas; }
    public void setTotalDescargas(long totalDescargas) { this.totalDescargas = totalDescargas; }
}
