package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface do Spring Data JPA — o Spring cria a implementação automaticamente
// Só precisamos declarar os métodos que fogem do padrão (findAll, findById, save, deleteById já vêm de graça)
@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    // verifica se já existe um cliente com esse CPF/CNPJ no banco
    boolean existsByCpfCnpj(String cpfCnpj);
}

