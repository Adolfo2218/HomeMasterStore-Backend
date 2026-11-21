package cl.duoc.homemaster.producto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "CRUD de productos de Home Master Store")
public class ProductoController {
    private final ProductoRepository repo;
    public ProductoController(ProductoRepository repo) {this.repo=repo;}

    @GetMapping
    public List<Producto> listar(@RequestParam(required=false) String categoria) {
        return categoria == null || categoria.isBlank() ? repo.findAll() : repo.findByCategoriaIgnoreCase(categoria);
    }
    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name="bearerAuth")
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody Producto producto) {return repo.save(producto);}
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name="bearerAuth")
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto cambios) {
        Producto p=buscar(id);
        p.setNombre(cambios.getNombre());p.setPrecio(cambios.getPrecio());p.setImagen(cambios.getImagen());
        p.setCategoria(cambios.getCategoria());p.setEtiqueta(cambios.getEtiqueta());
        return repo.save(p);
    }
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name="bearerAuth")
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Producto no encontrado");repo.deleteById(id);}
}
