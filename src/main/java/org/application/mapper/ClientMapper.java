package org.application.mapper;

import org.application.entity.Client;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;

public interface ClientMapper {
    Client map (ClientDto clientDto);

    ClientDto map (Client client);
    Client map (ClientInDto clientInDto);
}
