# Home Master Store — Backend, etapa 1

API REST académica creada con **Spring Boot 3.5.14, Java 17+, Spring Data JPA, H2 y Swagger UI**.
Incluye el catálogo original de **16 productos**. Es el **primer hito** de EP3: todavía **NO incluye JWT, usuarios, roles ni conexión activa desde React**. Los endpoints de escritura están abiertos: **solo ejecutar localmente**, nunca exponer a Internet.

## Ejecutar fácil (H2 local, sin instalar MySQL)
1. Instalar JDK 17 o 21 y Maven 3.6.3+ (`java -version` y `mvn -version`).
2. Abrir CMD en esta carpeta y ejecutar `mvn spring-boot:run`.
3. Visitar http://localhost:8080/swagger-ui.html para ver y probar la API.
4. Visitar http://localhost:8080/api/productos para ver los productos en JSON.
5. Los datos persisten en la carpeta local `datos/` (ignorada por Git).

## Usar MySQL (opcional)
1. Con Docker instalado: `docker compose up -d`.
2. Ejecutar `mvn spring-boot:run -Dspring-boot.run.profiles=mysql`.
3. La configuración está en `src/main/resources/application-mysql.properties`. Cambiar contraseñas para cualquier entorno real; las incluidas son **solo de laboratorio**.

## Endpoints
| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos?categoria=Semillas` | Filtra por categoría |
| GET | `/api/productos/{id}` | Detalle (404 si no existe) |
| POST | `/api/productos` | Crear (201) |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar (204) |

Ejemplo POST JSON: `{"nombre":"Semillas de zapallo","precio":2500,"imagen":"img/1.jpg","categoria":"Semillas","etiqueta":"Nuevo"}`

## Pruebas
`mvn test` realiza dos comprobaciones sobre la API usando MockMvc y una base H2 local de prueba.

## Próximo hito
1. Agregar usuarios persistentes con contraseñas BCrypt.
2. Spring Security, roles y JWT, incluida renovación de sesión.
3. Sustituir los datos locales del React por llamadas a `/api/productos` y la API de autenticación.
4. Probar flujos de integración y generar capturas para la documentación.

**Nota:** los productos con precio 0 provienen del catálogo original y deben corregirse antes de considerar la tienda lista para venta.
