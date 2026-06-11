package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Client;
import java.util.List;

// Interface que define o contrato do que a aplicação sabe fazer com Clientes
// O controller só fala com essa interface — nunca com o ClientService diretamente
public interface ClientServicePort {
    Client createClient(Client client);
    List<Client> findAllClients();
    Client findClientById(Long id);
    Client updateClient(Long id, Client client);
    void deleteClient(Long id);
}

