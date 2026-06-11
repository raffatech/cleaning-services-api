package com.example.cleaningservices.infrastructure.adapters.outbound.nfse;

import com.example.cleaningservices.application.ports.outbound.NfseGatewayPort;
import com.example.cleaningservices.domain.model.Client;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.ServiceInvoice;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

// =====================================================================
// ADAPTER SIMULADO — usado quando não há credenciais do Focus NFe
// Ativo somente com o perfil "mock" no application.properties:
//   spring.profiles.active=mock
//
// Para usar o Focus NFe de verdade (produção), trocar para:
//   spring.profiles.active=prod
// =====================================================================
@Profile("mock")
@Component
public class MockNfseAdapter implements NfseGatewayPort {

    @Override
    public ServiceInvoice emit(ServiceInvoice invoice, Emitter emitter, Client client) {

        // simula o número da nota (prefeitura retornaria um número real)
        String numeroSimulado = String.valueOf((int) (Math.random() * 90000) + 10000);

        // simula o código de verificação (hash que a prefeitura gera)
        String codigoVerificacao = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // simula a referência interna do Focus NFe
        String referencia = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // simula a URL do PDF da nota (Focus NFe retornaria uma URL real)
        String urlSimulada = "https://mock.focusnfe.com.br/nfse/" + referencia + ".pdf";

        // preenche a invoice com os dados simulados
        invoice.setFocusNfeReference(referencia);
        invoice.setInvoiceNumber(numeroSimulado);
        invoice.setVerificationCode(codigoVerificacao);
        invoice.setInvoiceUrl(urlSimulada);

        // log visual para saber que está em modo simulado
        System.out.println("==========================================================");
        System.out.println("[MOCK NFS-e] Nota fiscal SIMULADA gerada com sucesso!");
        System.out.println("[MOCK NFS-e] Emitente : " + emitter.getCompanyName());
        System.out.println("[MOCK NFS-e] Cliente  : " + client.getName());
        System.out.println("[MOCK NFS-e] Valor    : R$ " + invoice.getValue());
        System.out.println("[MOCK NFS-e] Número   : " + numeroSimulado);
        System.out.println("[MOCK NFS-e] Cód. Ver.: " + codigoVerificacao);
        System.out.println("[MOCK NFS-e] URL      : " + urlSimulada);
        System.out.println("==========================================================");

        return invoice;
    }
}

