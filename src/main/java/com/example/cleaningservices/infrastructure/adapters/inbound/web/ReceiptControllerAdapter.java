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

@RestController
@RequestMapping("/receipts")
@Tag(name = "Receipts", description = "Receipt generation endpoints")
public class ReceiptControllerAdapter {

    private final ReceiptServicePort receiptServicePort;

    public ReceiptControllerAdapter(ReceiptServicePort receiptServicePort) {
        this.receiptServicePort = receiptServicePort;
    }

    @PostMapping
    @Operation(summary = "Generate a receipt PDF")
    @ApiResponse(responseCode = "200", description = "PDF generated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Emitter not found")
    public ResponseEntity<byte[]> generateReceipt(@Valid @RequestBody ReceiptRequest request) {

        // monta o modelo de domínio a partir do request
        Receipt receipt = new Receipt(
                request.getReceiptNumber(),
                request.getClientName(),
                request.getValue(),
                request.getEmitterId(),
                null
        );

        // gera o PDF
        byte[] pdf = receiptServicePort.generateReceipt(receipt);

        // configura o header para download do arquivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "recibo-" + request.getClientName() + "-"+ request.getReceiptNumber() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
