package org.application.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.application.constant.PathConstants;
import org.application.controller.ClientControllerApi;
import org.application.entity.Client;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;
import org.application.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Slf4j
public class ClientControllerImpl implements ClientControllerApi {
    private final ClientService clientService;

    public ClientControllerImpl(ClientService clientService){
        this.clientService = clientService;
    }

    public String testDePrueba(){
        System.out.println("hola");
        return "Hola Mundo";
    }


    @Override
    public ResponseEntity<ClientDto> findClient(Long id){
        return new ResponseEntity<>(clientService.findClient(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClientDto> createClient(ClientInDto clientInDto){
        var savedClient = clientService.createClient(clientInDto);
        return ResponseEntity.created(URI.create(PathConstants.CLIENT_ROUTE))
                .contentType(MediaType.APPLICATION_JSON).body(savedClient);
    }
}
