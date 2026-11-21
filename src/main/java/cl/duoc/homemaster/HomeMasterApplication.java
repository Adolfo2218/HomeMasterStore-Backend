package cl.duoc.homemaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@io.swagger.v3.oas.annotations.security.SecurityScheme(name="bearerAuth",type=io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,scheme="bearer",bearerFormat="JWT")
@SpringBootApplication
public class HomeMasterApplication {
    public static void main(String[] args) { SpringApplication.run(HomeMasterApplication.class, args); }
}
