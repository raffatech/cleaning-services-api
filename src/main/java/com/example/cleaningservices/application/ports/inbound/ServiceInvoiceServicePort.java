package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.ServiceInvoice;
import java.util.List;

// Contrato de entrada para emissão de NFS-e
// O controller fala com essa interface — nunca diretamente com o service
public interface ServiceInvoiceServicePort {
    // emite a NFS-e e salva no banco
    ServiceInvoice emitInvoice(ServiceInvoice invoice);
    // busca o histórico de notas fiscais
    List<ServiceInvoice> findAllInvoices();
    ServiceInvoice findInvoiceById(Long id);
}
