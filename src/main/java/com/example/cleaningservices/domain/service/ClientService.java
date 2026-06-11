package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.ClientServicePort;
import com.example.cleaningservices.application.ports.outbound.ClientRepositoryPort;
import com.example.cleaningservices.domain.exception.ClientNotFoundException;
import com.example.cleaningservices.domain.exception.ValidationException;
import com.example.cleaningservices.domain.model.Client;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service diz ao Spring: "essa classe tem as regras de negócio, gerencia ela pra mim"
@Service
public class ClientService implements ClientServicePort {

    private final ClientRepositoryPort clientRepositoryPort;

    // injeção via construtor — nunca via @Autowired
    public ClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    // valida se já existe cliente com o mesmo CPF/CNPJ
    // id null = criação | id preenchido = atualização (ignora o próprio registro)
    private void validateClient(Client client, Long id) {
        Map<String, String> errors = new HashMap<>();

        Client existing = clientRepositoryPort.findById(id != null ? id : -1L).orElse(null);

        if (clientRepositoryPort.existsByCpfCnpj(client.getCpfCnpj())) {
            if (existing == null || !existing.getCpfCnpj().equals(client.getCpfCnpj())) {
                errors.put("cpfCnpj", "CPF/CNPJ: " + client.getCpfCnpj() + " already exists.");
            }
        }

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    @Override
    public Client createClient(Client client) {
        validateClient(client, null);
        return clientRepositoryPort.save(client);
    }

    @Override
    public List<Client> findAllClients() {
        return clientRepositoryPort.findAll();
    }

    @Override
    public Client findClientById(Long id) {
        return clientRepositoryPort.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public Client updateClient(Long id, Client client) {
        findClientById(id); // garante que o cliente existe antes de atualizar
        client.setId(id);
        validateClient(client, id);
        return clientRepositoryPort.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        findClientById(id); // garante que o cliente existe antes de deletar
        clientRepositoryPort.deleteById(id);
    }
}

