package org.application.entity.in;

//import javax.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Password is mandatory")
    @Size(min =6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "El email introducido debe ser valido")
    private String email;
}
