package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.entity.in.LoginDto;
import org.application.entity.out.AuthDto;
import org.application.entity.out.UserDto;
import org.application.entity.in.UserInDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.VERIFICATION_ROUTE)
public interface VerificationController {

    @Operation(tags = "verification", summary = "Crear nuevo usuario", description = "Crear nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")})
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> register (@Validated @RequestBody UserInDto userInDto);

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> login(@Validated @RequestBody LoginDto loginDto);

}
