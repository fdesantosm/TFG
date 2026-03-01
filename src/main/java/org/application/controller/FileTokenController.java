package org.application.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.application.constant.PathConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstants.TFG + PathConstants.FILE_TOKEN_ROUTE)
public interface FileTokenController {


    @ApiResponse(responseCode = "200", description = "Token creado correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes")
    @ApiResponse(responseCode = "500", description = "Error interno")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createPrivateToken (@RequestParam("title") String title,
                                               @RequestParam("duration") long duration);
}
