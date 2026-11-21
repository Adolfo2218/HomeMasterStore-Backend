package cl.duoc.homemaster.seguridad;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain apiSecurity(HttpSecurity http,JwtFilter filter)throws Exception {
  return http.csrf(csrf->csrf.disable())
    .cors(Customizer.withDefaults())
    .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(a->a
.requestMatchers(
    "/api/auth/registro",
    "/api/auth/login",
    "/api/auth/refresh",
    "/api/auth/logout",
    "/error"
).permitAll()     .requestMatchers(HttpMethod.GET,"/api/productos","/api/productos/**").permitAll()
     .requestMatchers("/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll()
     .requestMatchers("/h2-console/**").permitAll()
     .requestMatchers("/api/productos/**","/api/usuarios/**").hasRole("ADMIN")
     .anyRequest().authenticated())
    .headers(headers->headers.frameOptions(f->f.sameOrigin()))
    .addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class).build();
 }
}
