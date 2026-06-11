package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ServiceInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceInvoiceJpaRepository extends JpaRepository<ServiceInvoiceEntity, Long> {
    // busca todas as notas fiscais de um emitente específico
    List<ServiceInvoiceEntity> findByEmitterId(Long emitterId);
}

