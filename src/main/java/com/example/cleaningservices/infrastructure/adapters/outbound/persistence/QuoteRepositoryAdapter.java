package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.QuoteRepositoryPort;
import com.example.cleaningservices.domain.model.Quote;
import com.example.cleaningservices.domain.model.QuoteItem;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.QuoteEntity;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.QuoteItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QuoteRepositoryAdapter implements QuoteRepositoryPort {

    private final QuoteJpaRepository quoteJpaRepository;

    public QuoteRepositoryAdapter(QuoteJpaRepository quoteJpaRepository) {
        this.quoteJpaRepository = quoteJpaRepository;
    }

    // converte domínio → entidade JPA, incluindo os itens
    private QuoteEntity convertToEntity(Quote quote) {
        QuoteEntity entity = new QuoteEntity();
        entity.setId(quote.getId());
        entity.setQuoteNumber(quote.getQuoteNumber());
        entity.setEmitterId(quote.getEmitterId());
        entity.setClientName(quote.getClientName());
        entity.setMessage(quote.getMessage());
        entity.setDiscountPercent(quote.getDiscountPercent());
        entity.setPaymentConditions(quote.getPaymentConditions());
        entity.setPaymentMethods(quote.getPaymentMethods());
        entity.setDate(quote.getDate());

        // converte cada QuoteItem e liga ao QuoteEntity (necessário para o @ManyToOne)
        if (quote.getItems() != null) {
            List<QuoteItemEntity> itemEntities = quote.getItems().stream()
                    .map(item -> {
                        QuoteItemEntity itemEntity = new QuoteItemEntity();
                        itemEntity.setDescription(item.getDescription());
                        itemEntity.setValue(item.getValue());
                        itemEntity.setQuantity(item.getQuantity());
                        itemEntity.setUnit(item.getUnit());
                        itemEntity.setQuote(entity); // liga o item ao orçamento
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }

    // converte entidade JPA → domínio, incluindo os itens
    private Quote convertToDomain(QuoteEntity entity) {
        Quote quote = new Quote();
        quote.setId(entity.getId());
        quote.setQuoteNumber(entity.getQuoteNumber());
        quote.setEmitterId(entity.getEmitterId());
        quote.setClientName(entity.getClientName());
        quote.setMessage(entity.getMessage());
        quote.setDiscountPercent(entity.getDiscountPercent());
        quote.setPaymentConditions(entity.getPaymentConditions());
        quote.setPaymentMethods(entity.getPaymentMethods());
        quote.setDate(entity.getDate());

        if (entity.getItems() != null) {
            List<QuoteItem> items = entity.getItems().stream()
                    .map(itemEntity -> {
                        QuoteItem item = new QuoteItem();
                        item.setDescription(itemEntity.getDescription());
                        item.setValue(itemEntity.getValue());
                        item.setQuantity(itemEntity.getQuantity());
                        item.setUnit(itemEntity.getUnit());
                        return item;
                    })
                    .collect(Collectors.toList());
            quote.setItems(items);
        }

        return quote;
    }

    @Override
    public Quote save(Quote quote) {
        return convertToDomain(quoteJpaRepository.save(convertToEntity(quote)));
    }

    @Override
    public List<Quote> findAll() {
        return quoteJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Quote> findById(Long id) {
        return quoteJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public List<Quote> findByEmitterId(Long emitterId) {
        return quoteJpaRepository.findByEmitterId(emitterId).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
}

