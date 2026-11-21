package cl.duoc.homemaster.seguridad;
import cl.duoc.homemaster.usuario.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.*;import java.time.*;import java.util.*;import java.nio.charset.StandardCharsets;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 public record Registro(@NotBlank String nombre,@NotBlank String apellidos,@Email @NotBlank String correo,@NotBlank @Size(min=8,max=128) String password){}
 public record Login(@Email @NotBlank String correo,@NotBlank String password){}
 public record Perfil(Long id,String nombre,String apellidos,String correo,String rol){}
 public record Respuesta(String accessToken,Perfil usuario){}
 private final UsuarioRepository usuarios;private final RefreshSesionRepository sesiones;private final PasswordEncoder encoder;private final JwtService jwt;
 private final SecureRandom random=new SecureRandom();
 @Value("${hms.refresh.days:7}") private int refreshDays;
 public AuthController(UsuarioRepository usuarios,RefreshSesionRepository sesiones,PasswordEncoder encoder,JwtService jwt){this.usuarios=usuarios;this.sesiones=sesiones;this.encoder=encoder;this.jwt=jwt;}
 private String normalizar(String correo){return correo.strip().toLowerCase(Locale.ROOT);}
 private Perfil perfil(Usuario u){return new Perfil(u.getId(),u.getNombre(),u.getApellidos(),u.getCorreo(),u.getRol().name());}
 private String hash(String text){try{byte[] h=MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));return HexFormat.of().formatHex(h);}catch(Exception e){throw new IllegalStateException(e);}}
 private String crearRefresh(Usuario u){byte[] bytes=new byte[32];random.nextBytes(bytes);String token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);RefreshSesion sesion=new RefreshSesion();sesion.setUsuario(u);sesion.setTokenHash(hash(token));sesion.setVence(Instant.now().plus(Duration.ofDays(refreshDays)));sesiones.save(sesion);return token;}
 private ResponseCookie cookie(String refresh){return ResponseCookie.from("hms_refresh",refresh).httpOnly(true).secure(false).sameSite("Lax").path("/api/auth").maxAge(Duration.ofDays(refreshDays)).build();}
 private ResponseCookie borrarCookie(){return ResponseCookie.from("hms_refresh","").httpOnly(true).secure(false).sameSite("Lax").path("/api/auth").maxAge(0).build();}
 private String extraer(HttpServletRequest req){Cookie[] cs=req.getCookies();if(cs!=null)for(Cookie c:cs)if(c.getName().equals("hms_refresh"))return c.getValue();return null;}
 private ResponseEntity<Respuesta> respuesta(Usuario u){return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie(crearRefresh(u)).toString()).body(new Respuesta(jwt.generar(u),perfil(u)));}
 @PostMapping("/registro") @Transactional
 public ResponseEntity<Respuesta> registro(@Valid @RequestBody Registro r){String correo=normalizar(r.correo());if(usuarios.existsByCorreo(correo))throw new ResponseStatusException(HttpStatus.CONFLICT,"Correo ya registrado"); Usuario u=new Usuario();u.setNombre(r.nombre().strip());u.setApellidos(r.apellidos().strip());u.setCorreo(correo);u.setPasswordHash(encoder.encode(r.password()));u.setRol(Rol.CLIENTE);usuarios.save(u);return respuesta(u);}
 @PostMapping("/login")
 public ResponseEntity<Respuesta> login(@Valid @RequestBody Login r){Usuario u=usuarios.findByCorreo(normalizar(r.correo())).orElse(null);if(u==null||!u.isHabilitado()||!encoder.matches(r.password(),u.getPasswordHash()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas");return respuesta(u);}
 @GetMapping("/me") public Perfil me(@AuthenticationPrincipal Usuario u){return perfil(u);}
 @PostMapping("/refresh") @Transactional
 public ResponseEntity<Respuesta> refresh(HttpServletRequest req){String token=extraer(req);if(token==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión no encontrada");RefreshSesion sesion=sesiones.findById(hash(token)).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión inválida"));sesiones.delete(sesion);if(sesion.getVence().isBefore(Instant.now())||!sesion.getUsuario().isHabilitado())throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión caducada");return respuesta(sesion.getUsuario());}
 @PostMapping("/logout") @Transactional
 public ResponseEntity<Void> logout(HttpServletRequest req){String token=extraer(req);if(token!=null)sesiones.deleteById(hash(token));return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE,borrarCookie().toString()).build();}
}
