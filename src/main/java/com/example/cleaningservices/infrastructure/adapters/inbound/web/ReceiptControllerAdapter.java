package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.ReceiptServicePort;
import com.example.cleaningservices.domain.model.Receipt;
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
@RequestMapping("/receipts")
@Tag(name = "Receipts", description = "Geração e histórico de recibos")
public class ReceiptControllerAdapter {

    private final ReceiptServicePort receiptServicePort;

    public ReceiptControllerAdapter(ReceiptServicePort receiptServicePort) {
        this.receiptServicePort = receiptServicePort;
    }

    private ReceiptResponse convertToResponse(Receipt receipt) {
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getReceiptNumber(),
                receipt.getClientName(),
                receipt.getValue(),
                receipt.getEmitterId(),
                receipt.getDate()
        );
    }

    @PostMapping
    @Operation(summary = "Gerar recibo PDF", description = "Gera o PDF do recibo e salva o registro no banco")
    @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Emitente não encontrado")
    public ResponseEntity<byte[]> generateReceipt(@Valid @RequestBody ReceiptRequest request) {
        Receipt receipt = new Receipt(
                request.getReceiptNumber(),
                request.getClientName(),
                request.getValue(),
                request.getEmitterId(),
                null
        );

        byte[] pdf = receiptServicePort.generateReceipt(receipt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "recibo-" + request.getClientName() + "-" + request.getReceiptNumber() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping
    @Operation(summary = "Listar todos os recibos gerados")
    @ApiResponse(responseCode = "200", description = "Lista de recibos")
    public ResponseEntity<List<ReceiptResponse>> findAll() {
        List<ReceiptResponse> responses = receiptServicePort.findAllReceipts()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recibo por ID")
    @ApiResponse(responseCode = "200", description = "Recibo encontrado")
    @ApiResponse(responseCode = "404", description = "Recibo não encontrado")
    public ResponseEntity<ReceiptResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToResponse(receiptServicePort.findReceiptById(id)));
    }
}
