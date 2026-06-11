package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.ClientServicePort;
import com.example.cleaningservices.domain.model.Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// @RestController = controller que devolve JSON automaticamente
// @RequestMapping = prefixo de todas as rotas desse controller
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Client management endpoints")
public class ClientControllerAdapter {

    private final ClientServicePort clientServicePort;

    // injeção via construtor
    public ClientControllerAdapter(ClientServicePort clientServicePort) {
        this.clientServicePort = clientServicePort;
    }

    // converte domínio → DTO de resposta
    private ClientResponse converteToResponse(Client client) {
        return new ClientResponse(
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

    // converte DTO de entrada → domínio
    private Client converteToDomain(ClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setCpfCnpj(request.getCpfCnpj());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setZipCode(request.getZipCode());
        client.setStreet(request.getStreet());
        client.setNumber(request.getNumber());
        client.setComplement(request.getComplement());
        client.setNeighborhood(request.getNeighborhood());
        client.setCity(request.getCity());
        client.setState(request.getState());
        client.setMunicipalRegistration(request.getMunicipalRegistration());
        return client;
    }

    @PostMapping
    @Operation(summary = "Create a new client")
    @ApiResponse(responseCode = "201", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "409", description = "CPF/CNPJ already exists")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        Client created = clientServicePort.createClient(converteToDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(converteToResponse(created));
    }

    @GetMapping
    @Operation(summary = "List all clients")
    @ApiResponse(responseCode = "200", description = "Client list")
    public ResponseEntity<List<ClientResponse>> findAllClients() {
        List<ClientResponse> clients = clientServicePort.findAllClients()
                .stream()
                .map(this::converteToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find client by ID")
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<ClientResponse> findClientById(@PathVariable Long id) {
        return ResponseEntity.ok(converteToResponse(clientServicePort.findClientById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client by ID")
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "409", description = "CPF/CNPJ already exists")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,
                                                       @Valid @RequestBody ClientRequest request) {
        Client updated = clientServicePort.updateClient(id, converteToDomain(request));
        return ResponseEntity.ok(converteToResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID")
    @ApiResponse(responseCode = "204", description = "Client deleted successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientServicePort.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}

