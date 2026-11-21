package cl.duoc.homemaster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class ProductoControllerTest {
 @Autowired MockMvc mvc;
 @Test void catalogoDevuelveProductos() throws Exception {
   mvc.perform(get("/api/productos")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(16));
 }
 @Test void productoInexistenteDa404() throws Exception {
   mvc.perform(get("/api/productos/9999999")).andExpect(status().isNotFound());
 }
}
