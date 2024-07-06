package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.USER_ROUTE)
public interface UserControllerApi {



    @GetMapping(value = "/prueba")
    public String testDePrueba();


    @Operation(tags = "usuarios", summary = "Crear nuevo usuario", description = "Crear nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody UserInDto userInDto);


    @Operation(tags = "usuarios", summary = "Buscar usuario por id", description = "Buscar usuario por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "404",  description = "Usuario no encontrado")})
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserDto> findUser(@PathVariable(required = true, name = "id") Long id);


}
