package cl.duoc.homemaster.producto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity @Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false) private String nombre;
    @NotNull @DecimalMin("0.0") @Column(nullable = false, precision = 12, scale = 2) private BigDecimal precio;
    @NotBlank @Column(nullable = false) private String imagen;
    @NotBlank @Column(nullable = false) private String categoria;
    private String etiqueta;

    public Producto() {}
    public Producto(String nombre, BigDecimal precio, String imagen, String categoria, String etiqueta) {
        this.nombre=nombre; this.precio=precio; this.imagen=imagen; this.categoria=categoria; this.etiqueta=etiqueta;
    }
    public Long getId(){return id;}
    public String getNombre(){return nombre;} public void setNombre(String x){nombre=x;}
    public BigDecimal getPrecio(){return precio;} public void setPrecio(BigDecimal x){precio=x;}
    public String getImagen(){return imagen;} public void setImagen(String x){imagen=x;}
    public String getCategoria(){return categoria;} public void setCategoria(String x){categoria=x;}
    public String getEtiqueta(){return etiqueta;} public void setEtiqueta(String x){etiqueta=x;}
}
