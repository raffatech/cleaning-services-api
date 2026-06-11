package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.ReceiptRepositoryPort;
import com.example.cleaningservices.domain.model.Receipt;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.ReceiptEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReceiptRepositoryAdapter implements ReceiptRepositoryPort {

    private final ReceiptJpaRepository receiptJpaRepository;

    public ReceiptRepositoryAdapter(ReceiptJpaRepository receiptJpaRepository) {
        this.receiptJpaRepository = receiptJpaRepository;
    }

    private ReceiptEntity convertToEntity(Receipt receipt) {
        ReceiptEntity entity = new ReceiptEntity();
        entity.setId(receipt.getId());
        entity.setReceiptNumber(receipt.getReceiptNumber());
        entity.setClientName(receipt.getClientName());
        entity.setValue(receipt.getValue());
        entity.setEmitterId(receipt.getEmitterId());
        entity.setDate(receipt.getDate());
        return entity;
    }

    private Receipt convertToDomain(ReceiptEntity entity) {
        Receipt receipt = new Receipt();
        receipt.setId(entity.getId());
        receipt.setReceiptNumber(entity.getReceiptNumber());
        receipt.setClientName(entity.getClientName());
        receipt.setValue(entity.getValue());
        receipt.setEmitterId(entity.getEmitterId());
        receipt.setDate(entity.getDate());
        return receipt;
    }

    @Override
    public Receipt save(Receipt receipt) {
        return convertToDomain(receiptJpaRepository.save(convertToEntity(receipt)));
    }

    @Override
    public List<Receipt> findAll() {
        return receiptJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Receipt> findById(Long id) {
        return receiptJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public List<Receipt> findByEmitterId(Long emitterId) {
        return receiptJpaRepository.findByEmitterId(emitterId).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
}

