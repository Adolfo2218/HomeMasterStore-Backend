package cl.duoc.homemaster.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** API administrativa. El acceso ADMIN se valida en SecurityConfig, no en React. */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioAdminController {
    public record UsuarioVista(Long id, String nombre, String apellidos, String correo, Rol rol, boolean habilitado) {}
    public record CrearUsuario(
        @NotBlank String nombre,
        @NotBlank String apellidos,
        @NotBlank @Email String correo,
        @NotBlank @Size(min = 12, max = 128) String password,
        Rol rol
    ) {}
    public record CambiarEstado(boolean habilitado) {}

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;

    public UsuarioAdminController(UsuarioRepository usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    private UsuarioVista vista(Usuario u) {
        return new UsuarioVista(u.getId(), u.getNombre(), u.getApellidos(), u.getCorreo(), u.getRol(), u.isHabilitado());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<UsuarioVista> listar() {
        return usuarios.findAll().stream()
            .sorted(Comparator.comparing(Usuario::getId))
            .map(this::vista).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UsuarioVista crear(@Valid @RequestBody CrearUsuario datos) {
        String correo = datos.correo().strip().toLowerCase(Locale.ROOT);
        if (usuarios.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(datos.nombre().strip());
        u.setApellidos(datos.apellidos().strip());
        u.setCorreo(correo);
        u.setPasswordHash(encoder.encode(datos.password()));
        // Solo ADMIN puede invocar este endpoint; el rol se valida en servidor.
        u.setRol(datos.rol() == null ? Rol.CLIENTE : datos.rol());
        u.setHabilitado(true);
        return vista(usuarios.save(u));
    }

    @PatchMapping("/{id}/estado")
    @Transactional
    public UsuarioVista cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstado datos, Authentication autenticacion) {
        Usuario u = usuarios.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Usuario actor = (Usuario) autenticacion.getPrincipal();
        if (!datos.habilitado()) {
            if (u.getId().equals(actor.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes deshabilitar tu propia cuenta");
            }
            if (u.getRol() == Rol.ADMIN && usuarios.findAll().stream()
                    .noneMatch(x -> !x.getId().equals(id) && x.getRol() == Rol.ADMIN && x.isHabilitado())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe permanecer al menos un administrador activo");
            }
        }
        u.setHabilitado(datos.habilitado());
        return vista(usuarios.save(u));
    }
}
