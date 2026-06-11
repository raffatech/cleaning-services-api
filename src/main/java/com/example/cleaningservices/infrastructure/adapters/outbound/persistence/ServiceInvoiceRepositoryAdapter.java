package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.ServiceInvoiceRepositoryPort;
import com.example.cleaningservices.domain.model.ServiceInvoice;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ServiceInvoiceEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServiceInvoiceRepositoryAdapter implements ServiceInvoiceRepositoryPort {

    private final ServiceInvoiceJpaRepository serviceInvoiceJpaRepository;

    public ServiceInvoiceRepositoryAdapter(ServiceInvoiceJpaRepository serviceInvoiceJpaRepository) {
        this.serviceInvoiceJpaRepository = serviceInvoiceJpaRepository;
    }

    private ServiceInvoiceEntity convertToEntity(ServiceInvoice invoice) {
        ServiceInvoiceEntity entity = new ServiceInvoiceEntity();
        entity.setId(invoice.getId()); // preserva o id ao atualizar
        entity.setEmitterId(invoice.getEmitterId());
        entity.setClientId(invoice.getClientId());
        entity.setValue(invoice.getValue());
        entity.setServiceDescription(invoice.getServiceDescription());
        entity.setServiceCode(invoice.getServiceCode());
        entity.setCityOfService(invoice.getCityOfService());
        entity.setIssueDate(invoice.getIssueDate());
        entity.setStatus(invoice.getStatus());
        entity.setInvoiceNumber(invoice.getInvoiceNumber());
        entity.setVerificationCode(invoice.getVerificationCode());
        entity.setInvoiceUrl(invoice.getInvoiceUrl());
        entity.setFocusNfeReference(invoice.getFocusNfeReference());
        return entity;
    }

    private ServiceInvoice convertToDomain(ServiceInvoiceEntity entity) {
        ServiceInvoice invoice = new ServiceInvoice();
        invoice.setId(entity.getId()); // mapeia o id gerado pelo banco
        invoice.setEmitterId(entity.getEmitterId());
        invoice.setClientId(entity.getClientId());
        invoice.setValue(entity.getValue());
        invoice.setServiceDescription(entity.getServiceDescription());
        invoice.setServiceCode(entity.getServiceCode());
        invoice.setCityOfService(entity.getCityOfService());
        invoice.setIssueDate(entity.getIssueDate());
        invoice.setStatus(entity.getStatus());
        invoice.setInvoiceNumber(entity.getInvoiceNumber());
        invoice.setVerificationCode(entity.getVerificationCode());
        invoice.setInvoiceUrl(entity.getInvoiceUrl());
        invoice.setFocusNfeReference(entity.getFocusNfeReference());
        return invoice;
    }

    @Override
    public ServiceInvoice save(ServiceInvoice invoice) {
        return convertToDomain(serviceInvoiceJpaRepository.save(convertToEntity(invoice)));
    }

    @Override
    public List<ServiceInvoice> findAll() {
        return serviceInvoiceJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceInvoice> findById(Long id) {
        return serviceInvoiceJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public List<ServiceInvoice> findByEmitterId(Long emitterId) {
        return serviceInvoiceJpaRepository.findByEmitterId(emitterId).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
}

