package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.USER_ROUTE)
public interface UserControllerApi {


    @Operation(tags = "Prueba", summary = "Prueba de admin", description = "Probar si funciona el rol de administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Token reconocido correctamente",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")})
    @GetMapping(value = "/prueba", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> testDePrueba();

    @Operation(tags = "usuarios", summary = "Buscar usuario por id", description = "Buscar usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserDto> findUser(@PathVariable(required = true, name = "id") Long id);


}
