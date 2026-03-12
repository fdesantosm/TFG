package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.entity.in.PasswordDto;
import org.application.entity.in.UserInDto;
import org.application.entity.out.UserDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstants.TFG + PathConstants.USER_ROUTE)
public interface UserControllerApi {

  @Operation(tags = "Prueba", summary = "Prueba de admin", description = "Probar si funciona el rol de administrador")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Token reconocido correctamente",
      content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserEntity.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes")
  })
  @GetMapping(value = "/prueba", produces = MediaType.APPLICATION_JSON_VALUE)
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<String> testDePrueba();

  @Operation(tags = "users", summary = "Crear usuario", description = "Crear un usuario")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Usuario creado",
      content = @Content(schema = @Schema(implementation = UserEntity.class))),
    @ApiResponse(responseCode = "400", description = "Error creando el usuario"),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
    @ApiResponse(responseCode = "409", description = "Usuario con el nombre ya existente")
  })
  @PostMapping(produces = {"application/json"})
  public ResponseEntity<UserDto> createUser(@RequestBody UserInDto userInDto);

  @Operation(tags = "users", summary = "Buscar usuario ", description = "Buscar usuario por nombre o email")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
      content = @Content(schema = @Schema(implementation = UserEntity.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
    @GetMapping(produces = {"application/json"})
  public ResponseEntity<UserDto> findUser(@RequestParam(required = false) String username,
                                          @RequestParam(required = false) String email);


  @Operation(tags = "users", summary = "Buscar todos los usuarios", description = "Buscar todos los usuarios")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados",
      content = @Content(schema = @Schema(implementation = UserEntity.class))),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
    @ApiResponse(responseCode = "404", description = "Usuarios no encontrados")
  })
  @GetMapping(value = "/all", produces = {"application/json"})
  public ResponseEntity<List<UserDto>> findAllUsers();


  @Operation( tags = "users", summary = "Cambiar contraseña", description = "Cambiar la contraseña de su usuario"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Contraseña actualizada correctamente"),
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PatchMapping(value = "/me/password", consumes = "application/json")
  public ResponseEntity<Void> updatePassword( @AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody @Valid PasswordDto dto);


}
