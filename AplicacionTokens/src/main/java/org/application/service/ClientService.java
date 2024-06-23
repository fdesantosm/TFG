package org.application.service;

import org.application.entity.Client;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;

public interface ClientService {

    public ClientDto findClient(Long id);

    public ClientDto createClient(ClientInDto clientInDto);
}
