package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.QuoteServicePort;
import com.example.cleaningservices.domain.model.Quote;
import com.example.cleaningservices.domain.model.QuoteItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quotes")
@Tag(name = "Quotes", description = "Geração e histórico de orçamentos")
public class QuoteControllerAdapter {

    private final QuoteServicePort quoteServicePort;

    public QuoteControllerAdapter(QuoteServicePort quoteServicePort) {
        this.quoteServicePort = quoteServicePort;
    }

    // converte domínio → response (com itens aninhados e valores calculados)
    private QuoteResponse convertToResponse(Quote quote) {
        List<QuoteItemResponse> itemResponses = quote.getItems() == null ? List.of() :
                quote.getItems().stream()
                        .map(item -> new QuoteItemResponse(
                                item.getDescription(), item.getValue(),
                                item.getQuantity(), item.getUnit(), item.getTotal()))
                        .collect(Collectors.toList());

        return new QuoteResponse(
                quote.getId(), quote.getQuoteNumber(), quote.getEmitterId(),
                quote.getClientName(), quote.getMessage(), itemResponses,
                quote.getDiscountPercent(), quote.getDiscountAmount(),
                quote.getSubtotal(), quote.getFinalValue(),
                quote.getPaymentConditions(), quote.getPaymentMethods(), quote.getDate()
        );
    }

    @PostMapping
    @Operation(summary = "Gerar orçamento PDF", description = "Gera o PDF do orçamento e salva o registro no banco")
    @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Emitente não encontrado")
    public ResponseEntity<byte[]> generateQuote(@Valid @RequestBody QuoteRequest request) {
        Quote quote = new Quote();
        quote.setEmitterId(request.getEmitterId());
        quote.setQuoteNumber(request.getQuoteNumber());
        quote.setClientName(request.getClientName());
        quote.setMessage(request.getMessage());
        quote.setDiscountPercent(request.getDiscountPercent());
        quote.setPaymentConditions(request.getPaymentConditions());
        quote.setPaymentMethods(request.getPaymentMethods());
        quote.setItems(request.getItems().stream().map(i -> {
            QuoteItem item = new QuoteItem();
            item.setDescription(i.getDescription());
            item.setValue(i.getValue());
            item.setQuantity(i.getQuantity());
            item.setUnit(i.getUnit());
            return item;
        }).collect(Collectors.toList()));

        byte[] pdf = quoteServicePort.generateQuote(quote);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "orcamento-" + request.getQuoteNumber() + "-" + request.getClientName() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping
    @Operation(summary = "Listar todos os orçamentos gerados")
    @ApiResponse(responseCode = "200", description = "Lista de orçamentos")
    public ResponseEntity<List<QuoteResponse>> findAll() {
        List<QuoteResponse> responses = quoteServicePort.findAllQuotes()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orçamento por ID")
    @ApiResponse(responseCode = "200", description = "Orçamento encontrado")
    @ApiResponse(responseCode = "404", description = "Orçamento não encontrado")
    public ResponseEntity<QuoteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToResponse(quoteServicePort.findQuoteById(id)));
    }
}
