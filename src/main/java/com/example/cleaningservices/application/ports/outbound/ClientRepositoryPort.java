package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Client;
import java.util.List;
import java.util.Optional;

// Interface que define o que a aplicação precisa do banco de dados para Clientes
// O service só fala com essa interface — nunca com o JPA diretamente
public interface ClientRepositoryPort {
    Client save(Client client);
    List<Client> findAll();
    Optional<Client> findById(Long id);
    void deleteById(Long id);
    boolean existsByCpfCnpj(String cpfCnpj);
}

