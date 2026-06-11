package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.ServiceInvoiceServicePort;
import com.example.cleaningservices.domain.model.ServiceInvoice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invoices")
@Tag(name = "Invoices", description = "Emissão e histórico de NFS-e")
public class ServiceInvoiceControllerAdapter {

    private final ServiceInvoiceServicePort serviceInvoiceServicePort;

    public ServiceInvoiceControllerAdapter(ServiceInvoiceServicePort serviceInvoiceServicePort) {
        this.serviceInvoiceServicePort = serviceInvoiceServicePort;
    }

    private ServiceInvoiceResponse convertToResponse(ServiceInvoice invoice) {
        ServiceInvoiceResponse response = new ServiceInvoiceResponse();
        response.setStatus(invoice.getStatus());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setVerificationCode(invoice.getVerificationCode());
        response.setInvoiceUrl(invoice.getInvoiceUrl());
        response.setValue(invoice.getValue());
        response.setServiceDescription(invoice.getServiceDescription());
        response.setIssueDate(invoice.getIssueDate());
        response.setFocusNfeReference(invoice.getFocusNfeReference());
        return response;
    }

    @PostMapping
    @Operation(summary = "Emitir NFS-e",
            description = "Emite a nota fiscal via Focus NFe e salva o registro no banco")
    @ApiResponse(responseCode = "200", description = "NFS-e emitida com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Emitente ou cliente não encontrado")
    @ApiResponse(responseCode = "422", description = "Nota rejeitada pelo Focus NFe / Prefeitura")
    public ResponseEntity<ServiceInvoiceResponse> emitInvoice(
            @Valid @RequestBody ServiceInvoiceRequest request) {

        ServiceInvoice invoice = new ServiceInvoice();
        invoice.setEmitterId(request.getEmitterId());
        invoice.setClientId(request.getClientId());
        invoice.setValue(request.getValue());
        invoice.setServiceDescription(request.getServiceDescription());
        invoice.setServiceCode(request.getServiceCode());
        invoice.setCityOfService(request.getCityOfService());

        return ResponseEntity.ok(convertToResponse(serviceInvoiceServicePort.emitInvoice(invoice)));
    }

    @GetMapping
    @Operation(summary = "Listar todas as notas fiscais emitidas")
    @ApiResponse(responseCode = "200", description = "Lista de NFS-e")
    public ResponseEntity<List<ServiceInvoiceResponse>> findAll() {
        List<ServiceInvoiceResponse> responses = serviceInvoiceServicePort.findAllInvoices()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar NFS-e por ID")
    @ApiResponse(responseCode = "200", description = "Nota fiscal encontrada")
    @ApiResponse(responseCode = "404", description = "Nota fiscal não encontrada")
    public ResponseEntity<ServiceInvoiceResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToResponse(serviceInvoiceServicePort.findInvoiceById(id)));
    }
}
