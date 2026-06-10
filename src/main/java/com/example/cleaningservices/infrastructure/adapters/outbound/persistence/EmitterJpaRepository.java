package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.EmitterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmitterJpaRepository extends JpaRepository<EmitterEntity, Long> {
    boolean existsByCpfCnpj(String cpfCnpj);
}

