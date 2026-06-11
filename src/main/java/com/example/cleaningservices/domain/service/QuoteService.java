package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.QuoteServicePort;
import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.application.ports.outbound.QuotePdfPort;
import com.example.cleaningservices.application.ports.outbound.QuoteRepositoryPort;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.exception.QuoteNotFoundException;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Quote;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuoteService implements QuoteServicePort {

    // injeta a porta de PDF — o serviço não sabe que é PDFBox, só sabe que gera PDFs
    private final QuotePdfPort quotePdfPort;
    private final EmitterRepositoryPort emitterRepositoryPort;
    private final QuoteRepositoryPort quoteRepositoryPort;

    public QuoteService(QuotePdfPort quotePdfPort,
                        EmitterRepositoryPort emitterRepositoryPort,
                        QuoteRepositoryPort quoteRepositoryPort) {
        this.quotePdfPort = quotePdfPort;
        this.emitterRepositoryPort = emitterRepositoryPort;
        this.quoteRepositoryPort = quoteRepositoryPort;
    }

    @Override
    public byte[] generateQuote(Quote quote) {
        quote.setDate(LocalDate.now());

        Emitter emitter = emitterRepositoryPort.findById(quote.getEmitterId())
                .orElseThrow(() -> new EmitterNotFoundException(quote.getEmitterId()));

        // gera o PDF primeiro — se falhar, não salva no banco
        byte[] pdf = quotePdfPort.generate(quote, emitter);

        // salva o orçamento no banco somente após gerar o PDF com sucesso
        quoteRepositoryPort.save(quote);

        return pdf;
    }

    @Override
    public List<Quote> findAllQuotes() {
        return quoteRepositoryPort.findAll();
    }

    @Override
    public Quote findQuoteById(Long id) {
        return quoteRepositoryPort.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
    }
}
