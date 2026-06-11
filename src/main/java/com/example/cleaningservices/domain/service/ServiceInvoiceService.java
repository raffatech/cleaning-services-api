package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.ServiceInvoiceServicePort;
import com.example.cleaningservices.application.ports.outbound.ClientRepositoryPort;
import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.application.ports.outbound.NfseGatewayPort;
import com.example.cleaningservices.application.ports.outbound.ServiceInvoiceRepositoryPort;
import com.example.cleaningservices.domain.exception.ClientNotFoundException;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.exception.ServiceInvoiceNotFoundException;
import com.example.cleaningservices.domain.model.Client;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.ServiceInvoice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceInvoiceService implements ServiceInvoiceServicePort {

    private final EmitterRepositoryPort emitterRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final NfseGatewayPort nfseGatewayPort;
    private final ServiceInvoiceRepositoryPort serviceInvoiceRepositoryPort;

    public ServiceInvoiceService(EmitterRepositoryPort emitterRepositoryPort,
                                 ClientRepositoryPort clientRepositoryPort,
                                 NfseGatewayPort nfseGatewayPort,
                                 ServiceInvoiceRepositoryPort serviceInvoiceRepositoryPort) {
        this.emitterRepositoryPort = emitterRepositoryPort;
        this.clientRepositoryPort = clientRepositoryPort;
        this.nfseGatewayPort = nfseGatewayPort;
        this.serviceInvoiceRepositoryPort = serviceInvoiceRepositoryPort;
    }

    @Override
    public ServiceInvoice emitInvoice(ServiceInvoice invoice) {
        invoice.setIssueDate(LocalDate.now());
        invoice.setStatus("PENDENTE");

        Emitter emitter = emitterRepositoryPort.findById(invoice.getEmitterId())
                .orElseThrow(() -> new EmitterNotFoundException(invoice.getEmitterId()));

        Client client = clientRepositoryPort.findById(invoice.getClientId())
                .orElseThrow(() -> new ClientNotFoundException(invoice.getClientId()));

        // envia para o Focus NFe (ou mock) via porta de saída
        ServiceInvoice emitted = nfseGatewayPort.emit(invoice, emitter, client);
        emitted.setStatus("EMITIDA");

        // salva a nota no banco após emissão bem-sucedida
        return serviceInvoiceRepositoryPort.save(emitted);
    }

    @Override
    public List<ServiceInvoice> findAllInvoices() {
        return serviceInvoiceRepositoryPort.findAll();
    }

    @Override
    public ServiceInvoice findInvoiceById(Long id) {
        return serviceInvoiceRepositoryPort.findById(id)
                .orElseThrow(() -> new ServiceInvoiceNotFoundException(id));
    }
}
