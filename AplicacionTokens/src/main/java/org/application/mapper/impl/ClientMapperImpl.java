package org.application.mapper.impl;

import org.application.entity.Client;
import org.application.entity.dto.ClientDto;
import org.application.entity.in.ClientInDto;
import org.application.mapper.ClientMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClientMapperImpl implements ClientMapper {


    public Client map(ClientDto clientDto){
        if (clientDto == null){
            return null;
        }
        var client = new Client();
        BeanUtils.copyProperties(clientDto, client);

        return client;
    }

    public ClientDto map(Client client){
        if (client == null){
            return null;
        }
        var clientDto = new ClientDto();
        BeanUtils.copyProperties(client, clientDto);

        return clientDto;
    }

    public Client map(ClientInDto clientInDto){
        if (clientInDto == null){
            return null;
        }
        var client = new Client();
        BeanUtils.copyProperties(clientInDto, client);
        return client;
    }
}
