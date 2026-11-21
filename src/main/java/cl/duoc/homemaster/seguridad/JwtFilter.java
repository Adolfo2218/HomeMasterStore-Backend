package cl.duoc.homemaster.seguridad;
import cl.duoc.homemaster.usuario.*;
import jakarta.servlet.*;import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UsuarioRepository usuarios;
 public JwtFilter(JwtService jwt,UsuarioRepository usuarios){this.jwt=jwt;this.usuarios=usuarios;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain) throws ServletException,IOException {
  String value=req.getHeader("Authorization");
  if(value!=null&&value.startsWith("Bearer ")&&SecurityContextHolder.getContext().getAuthentication()==null){
   try { Long id=jwt.usuarioId(value.substring(7)); Usuario u=usuarios.findById(id).orElse(null);
    if(u!=null&&u.isHabilitado())SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,null,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRol().name()))));
   } catch(Exception ignored) { /* token caducado o invalido: peticion no autenticada */ }
  }
  chain.doFilter(req,res);
 }
}
