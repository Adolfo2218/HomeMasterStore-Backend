package cl.duoc.homemaster.usuario;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="usuarios", uniqueConstraints=@UniqueConstraint(columnNames="correo"))
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String nombre;
 @Column(nullable=false) private String apellidos;
 @Column(nullable=false, unique=true) private String correo;
 @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Rol rol=Rol.CLIENTE;
 @Column(nullable=false) private boolean habilitado=true;
 public Long getId(){return id;} public String getNombre(){return nombre;} public void setNombre(String s){nombre=s;}
 public String getApellidos(){return apellidos;} public void setApellidos(String s){apellidos=s;}
 public String getCorreo(){return correo;} public void setCorreo(String s){correo=s;}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String s){passwordHash=s;}
 public Rol getRol(){return rol;} public void setRol(Rol v){rol=v;}
 public boolean isHabilitado(){return habilitado;} public void setHabilitado(boolean v){habilitado=v;}
}
