package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.ClientRepositoryPort;
import com.example.cleaningservices.domain.model.Client;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Component diz ao Spring: "gerencie essa classe"
// Essa classe faz a ponte entre o domínio (Client) e o banco de dados (ClientEntity)
@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientJpaRepository clientJpaRepository;

    public ClientRepositoryAdapter(ClientJpaRepository clientJpaRepository) {
        this.clientJpaRepository = clientJpaRepository;
    }

    // converte domínio → entidade JPA (para salvar no banco)
    private ClientEntity convertToEntity(Client client) {
        return new ClientEntity(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getPhone(),
                client.getZipCode(),
                client.getStreet(),
                client.getNumber(),
                client.getComplement(),
                client.getNeighborhood(),
                client.getCity(),
                client.getState(),
                client.getMunicipalRegistration()
        );
    }

    // converte entidade JPA → domínio (para devolver ao serviço)
    private Client convertToDomain(ClientEntity entity) {
        return new Client(
                entity.getId(),
                entity.getName(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getZipCode(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState(),
                entity.getMunicipalRegistration()
        );
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = convertToEntity(client);
        ClientEntity saved = clientJpaRepository.save(entity);
        return convertToDomain(saved);
    }

    @Override
    public List<Client> findAll() {
        return clientJpaRepository.findAll()
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public void deleteById(Long id) {
        clientJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCpfCnpj(String cpfCnpj) {
        return clientJpaRepository.existsByCpfCnpj(cpfCnpj);
    }
}
