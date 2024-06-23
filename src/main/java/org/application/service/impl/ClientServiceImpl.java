package org.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;
import org.application.exception.ResponseException;
import org.application.mapper.ClientMapper;
import org.application.repository.ClientRepository;
import org.application.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper mapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }


    @Override
    public ClientDto findClient(Long id) {
        return Optional.of(id)
                .flatMap(clientRepository::findById)
                .map(mapper::map)
                .orElseThrow(() -> new ResponseException(
                        "No se ha encontrado ningun cliente con el id: " +  id, HttpStatus.NOT_FOUND));
    }

    public ClientDto createClient(ClientInDto clientInDto) {
        return Optional.of(clientInDto)
                .map(this::validateName)
                .map(mapper::map)
                .map(clientRepository::save)
                .map(mapper::map)
                .orElseThrow(() -> new ResponseException(
                        "Bad mapping", HttpStatus.BAD_REQUEST));
    }

    private ClientInDto validateName(ClientInDto clientInDto) {
        return Optional.of(clientInDto.getName())
                .map(clientRepository::findByNameEqualsIgnoreCase)
                .filter(Optional::isEmpty)
                .map(given -> clientInDto)
                .orElseThrow(() -> new ResponseException(
                        "Cliente con el nombre '" + clientInDto.getName() + "' ya existe.", HttpStatus.BAD_REQUEST));
    }
}
