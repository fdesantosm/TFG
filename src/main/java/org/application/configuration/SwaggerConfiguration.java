package org.application.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "TFG de Fernando de Santos",
        version = "1.0",
        description = "Esta es una API de ejemplo para mostrar el uso del sistema",
        contact = @Contact(
                name = "Fernando de Santos",
                email = "f.desantosm@alumnos.upm.es"
        ),
        license = @License(
                name = "MIT License",
                url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class SwaggerConfiguration {
    public OpenAPI api (){
        return new OpenAPI();
    }
}
