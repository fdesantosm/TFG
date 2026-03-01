package org.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.application.constant.PathConstants;
import org.application.entity.UserEntity;
import org.application.entity.out.FileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping(PathConstants.TFG + PathConstants.FILE_ROUTE)
public interface FileController {

    @Operation(tags = "files", summary = "Subir archivo", description = "Subir Archivo a la plataforma")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Archivo subido correctamente",
        content = @Content(schema = @Schema(implementation = UserEntity.class))),
      @ApiResponse(responseCode = "400", description = "El archivo no es valido"),
      @ApiResponse(responseCode = "401", description = "No autenticado"),
      @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
      @ApiResponse(responseCode = "409", description = "El archivo ya existe"),
      @ApiResponse(responseCode = "413", description = "El archivo es demasiado grande"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description);


    @Operation(tags = "files", summary = "Descargar archivo", description = "Descargar archivo")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Archivo descargado correctamente",
        content = @Content(schema = @Schema(implementation = UserEntity.class))),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes"),
    @ApiResponse(responseCode = "404", description = "El archivo no existe"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")})
    @GetMapping("/download/{token}")
    public ResponseEntity<String> downloadFile(@PathVariable String token) throws IOException;

    @Operation(tags = "files", summary = "Buscar archivos publicos de usuario", description = "Buscar archivos publicos del usuario con el username indicado")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Archivos encontrados",
        content = @Content(schema = @Schema(implementation = FileDto.class))),
      @ApiResponse(responseCode = "400", description = "Username no valido"),
      @ApiResponse(responseCode = "404", description = "El usuario no existe"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")})
    @GetMapping("/publicFiles/{username}")
    public ResponseEntity<List<FileDto>> findPublicFilesFromUser(@PathVariable String username);
}
