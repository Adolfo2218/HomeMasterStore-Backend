package cl.duoc.homemaster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.UUID;
@SpringBootTest @AutoConfigureMockMvc
class AuthApiTest {
 @Autowired MockMvc mvc;
 @Test void elRegistroDaRolClienteYNoExponeHash() throws Exception {
  String correo="prueba-"+UUID.randomUUID()+"@example.com";
  String json="{\"nombre\":\"Ana\",\"apellidos\":\"Perez\",\"correo\":\""+correo+"\",\"password\":\"ClaveDePrueba2026!\"}";
  mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON).content(json))
    .andExpect(status().isOk()).andExpect(jsonPath("$.usuario.rol").value("CLIENTE"))
    .andExpect(jsonPath("$.accessToken").isNotEmpty())
    .andExpect(jsonPath("$.usuario.passwordHash").doesNotExist());
 }
 @Test void clienteAnonimoNoPuedeCrearProductos()throws Exception {
  mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content("{}"))
   .andExpect(status().isForbidden());
 }
 @Test void productosPublicos()throws Exception {
  mvc.perform(get("/api/productos")).andExpect(status().isOk());
 }
}
