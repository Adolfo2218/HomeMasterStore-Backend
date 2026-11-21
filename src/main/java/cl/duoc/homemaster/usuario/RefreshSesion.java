package cl.duoc.homemaster.usuario;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="sesiones_refresh")
public class RefreshSesion {
 @Id @Column(length=64) private String tokenHash;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) private Usuario usuario;
 @Column(nullable=false) private Instant vence;
 public String getTokenHash(){return tokenHash;} public void setTokenHash(String s){tokenHash=s;}
 public Usuario getUsuario(){return usuario;} public void setUsuario(Usuario u){usuario=u;}
 public Instant getVence(){return vence;} public void setVence(Instant v){vence=v;}
}
