package cl.duoc.homemaster.seguridad;
import cl.duoc.homemaster.usuario.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
@Service
public class JwtService {
 private final SecretKey key; private final int minutes;
 public JwtService(@Value("${hms.jwt.secret:}") String secret,@Value("${hms.jwt.access-minutes:15}") int minutes){
  if(secret.isBlank()) throw new IllegalStateException("Falta HMS_JWT_SECRET: configura un secreto base64 aleatorio de 32 bytes o mas.");
  byte[] bytes;
  try { bytes=Base64.getDecoder().decode(secret); } catch(Exception e){throw new IllegalStateException("HMS_JWT_SECRET debe estar en Base64",e);}
  if(bytes.length<32)throw new IllegalStateException("HMS_JWT_SECRET debe tener como minimo 32 bytes aleatorios");
  this.key=Keys.hmacShaKeyFor(bytes);this.minutes=minutes;
 }
 public String generar(Usuario u){Instant ahora=Instant.now();return Jwts.builder().subject(u.getId().toString()).issuedAt(Date.from(ahora)).expiration(Date.from(ahora.plusSeconds(minutes*60L))).signWith(key,Jwts.SIG.HS256).compact();}
 public Long usuarioId(String jwt){return Long.parseLong(Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload().getSubject());}
}
