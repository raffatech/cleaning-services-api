package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Client;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.ServiceInvoice;

// ⭐ Porta de saída para o provedor de NFS-e (Focus NFe)
//
// Analogia: essa interface é o "guichê do cartório" — não importa qual cartório
// você usa, o que você entrega e o que recebe de volta é sempre o mesmo.
// Hoje usamos o Focus NFe. Se um dia mudar de provedor, só trocamos o adapter
// que implementa essa interface — o resto do sistema não muda nada.
public interface NfseGatewayPort {

    // Envia a nota fiscal para o Focus NFe e retorna com número e código preenchidos
    ServiceInvoice emit(ServiceInvoice invoice, Emitter emitter, Client client);
}

