package cl.duoc.homemaster.seguridad;
import cl.duoc.homemaster.usuario.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Locale;
@Configuration
public class AdminBootstrap {
 @Bean CommandLineRunner semillaAdmin(UsuarioRepository repo,PasswordEncoder encoder,@Value("${hms.admin.email:}") String email,@Value("${hms.admin.password:}") String password){
  return args->{ if(email.isBlank()||password.isBlank())return;
   if(password.length()<12)throw new IllegalStateException("HMS_ADMIN_PASSWORD debe tener al menos 12 caracteres");
   String correo=email.strip().toLowerCase(Locale.ROOT);
   if(repo.existsByCorreo(correo))return;
   Usuario u=new Usuario();u.setNombre("Administrador");u.setApellidos("Academico");u.setCorreo(correo);u.setPasswordHash(encoder.encode(password));u.setRol(Rol.ADMIN);repo.save(u);
  };
 }
}
