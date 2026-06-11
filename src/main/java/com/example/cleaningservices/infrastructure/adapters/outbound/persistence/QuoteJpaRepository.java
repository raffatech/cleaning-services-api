package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, Long> {
    // busca todos os orçamentos de um emitente específico
    List<QuoteEntity> findByEmitterId(Long emitterId);
}

