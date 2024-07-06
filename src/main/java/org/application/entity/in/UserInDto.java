package org.application.entity.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min =6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "El email introducido debe ser valido")
    private String email;
}
