package cl.duoc.homemaster.config;
import cl.duoc.homemaster.producto.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.List;
@Configuration
public class DatosIniciales {
  @Bean CommandLineRunner cargarProductos(ProductoRepository repo) {
    return args -> {
      if (repo.count() > 0) return;
      repo.saveAll(List.of(
            new Producto("Semillas de Tomate", new BigDecimal("0"), "img/1.jpg", "Semillas", "Nuevo"),
            new Producto("12 Sobres de Semillas", new BigDecimal("10"), "img/3.jpg", "Semillas", null),
            new Producto("Semillas de Manzana", new BigDecimal("0"), "img/2.jpg", "Semillas", null),
            new Producto("Semillas de Frutilla", new BigDecimal("0"), "img/7.jpg", "Semillas", "Nuevo"),
            new Producto("Quisque vulputate aliquam", new BigDecimal("0"), "img/3.jpg", "Packs", "Nuevo"),
            new Producto("Praesent nibh lorem, tempus quis est a", new BigDecimal("0"), "img/5.jpg", "Packs", "Nuevo"),
            new Producto("Nunc sit amet justo nec ipsum dolor.", new BigDecimal("0"), "img/6.jpg", "Packs", null),
            new Producto("Lorem ipsum dolor sit, amet consectetur", new BigDecimal("0"), "img/8.jpg", "Packs", null),
            new Producto("Quisque vulputate aliquam", new BigDecimal("0"), "img/1.jpg", "Otros", null),
            new Producto("Praesent nibh lorem, tempus quis est a", new BigDecimal("0"), "img/2.jpg", "Otros", null),
            new Producto("Nunc sit amet justo nec ipsum dolor.", new BigDecimal("0"), "img/3.jpg", "Otros", null),
            new Producto("Lorem ipsum dolor sit, amet consectetur", new BigDecimal("0"), "img/7.jpg", "Otros", null),
            new Producto("Quisque vulputate aliquam", new BigDecimal("0"), "img/1.jpg", "Ofertas", "Oferta"),
            new Producto("Praesent nibh lorem, tempus quis est a", new BigDecimal("0"), "img/2.jpg", "Ofertas", "Oferta"),
            new Producto("Nunc sit amet justo nec ipsum dolor.", new BigDecimal("0"), "img/7.jpg", "Ofertas", "Oferta"),
            new Producto("Lorem ipsum dolor sit, amet consectetur", new BigDecimal("0"), "img/8.jpg", "Ofertas", "Oferta")
      ));
    };
  }
}
