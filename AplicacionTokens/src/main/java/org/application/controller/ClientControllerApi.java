package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.application.constant.PathConstants;
import org.application.entity.Client;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.CLIENT_ROUTE)
public interface ClientControllerApi {



    @GetMapping(value = "/prueba")
    public String testDePrueba();


    @Operation(tags = "clientes", summary = "Crear nuevo cliente", description = "Crear nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado con éxito",
                    content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(@Validated @RequestBody ClientInDto clientInDto);


    @Operation(tags = "clientes", summary = "Buscar cliente por id", description = "Buscar cliente por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "404",  description = "Cliente no encontrado")})
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<ClientDto> findClient( @PathVariable(required = true, name = "id") Long id);


}
