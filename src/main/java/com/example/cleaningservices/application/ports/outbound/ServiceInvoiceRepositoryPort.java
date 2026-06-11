package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.ServiceInvoice;
import java.util.List;
import java.util.Optional;

// Define o que a aplicação precisa do banco para NFS-e emitidas
public interface ServiceInvoiceRepositoryPort {
    ServiceInvoice save(ServiceInvoice invoice);
    List<ServiceInvoice> findAll();
    Optional<ServiceInvoice> findById(Long id);
    List<ServiceInvoice> findByEmitterId(Long emitterId);
}

