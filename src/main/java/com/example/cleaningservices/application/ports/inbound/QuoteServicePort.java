package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Quote;
import java.util.List;

// Porta inbound — define o que o sistema sabe fazer com orçamentos
public interface QuoteServicePort {

    // recebe o orçamento e retorna o PDF em bytes para download
    byte[] generateQuote(Quote quote);
    // busca o histórico de orçamentos
    List<Quote> findAllQuotes();
    Quote findQuoteById(Long id);
}
