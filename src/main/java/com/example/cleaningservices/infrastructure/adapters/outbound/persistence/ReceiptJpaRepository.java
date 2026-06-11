package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReceiptJpaRepository extends JpaRepository<ReceiptEntity, Long> {
    // busca todos os recibos de um emitente específico
    List<ReceiptEntity> findByEmitterId(Long emitterId);
}

