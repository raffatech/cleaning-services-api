package com.example.cleaningservices.infrastructure.adapters.outbound.nfse;

import com.example.cleaningservices.application.ports.outbound.NfseGatewayPort;
import com.example.cleaningservices.domain.exception.InvoiceEmissionException;
import com.example.cleaningservices.domain.model.Client;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.ServiceInvoice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

// Adapter que implementa NfseGatewayPort usando a API do Focus NFe
// Documentação: https://focusnfe.com.br/documentacao/nfse
// Só é ativado quando o perfil "prod" está ativo no application.properties
@Profile("prod")
@Component
public class FocusNfeAdapter implements NfseGatewayPort {

    @Value("${focusnfe.token}")
    private String token;

    @Value("${focusnfe.ambiente}")
    private String ambiente;

    @Value("${focusnfe.url.homologacao}")
    private String urlHomologacao;

    @Value("${focusnfe.url.producao}")
    private String urlProducao;

    // código IBGE de São Paulo — fixo para este sistema
    private static final String COD_MUNICIPIO_SAO_PAULO = "3550308";

    // RestTemplate com timeout de 30s para conexão e 60s para leitura
    // Sem timeout, uma falha na API do Focus NFe travaria a requisição indefinidamente
    private final RestTemplate restTemplate;

    public FocusNfeAdapter() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30_000); // 30 segundos para conectar
        factory.setReadTimeout(60_000);    // 60 segundos para ler a resposta
        this.restTemplate = new RestTemplate(factory);
    }

    // retorna a URL correta dependendo do ambiente configurado
    private String getBaseUrl() {
        return "homologacao".equals(ambiente) ? urlHomologacao : urlProducao;
    }

    // monta o header de autenticação Basic do Focus NFe
    // o token vai como "usuário" e a senha fica vazia — padrão Focus NFe
    private HttpHeaders buildHeaders() {
        String credentials = token + ":";
        String encoded = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public ServiceInvoice emit(ServiceInvoice invoice, Emitter emitter, Client client) {
        // gera referência única para esta nota (Focus NFe exige)
        String reference = "NFS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        invoice.setFocusNfeReference(reference);

        // monta o payload JSON conforme documentação Focus NFe para NFS-e SP
        Map<String, Object> payload = buildPayload(invoice, emitter, client);

        String url = getBaseUrl() + "/v2/nfse?ref=" + reference;
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, buildHeaders());

        try {
            // exchange com ParameterizedTypeReference evita o uso de Map sem tipo (raw type)
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return parseResponse(invoice, response.getBody());

        } catch (HttpClientErrorException e) {
            throw new InvoiceEmissionException(
                    "Erro ao emitir NFS-e",
                    "Status: " + e.getStatusCode() + " — " + e.getResponseBodyAsString()
            );
        } catch (Exception e) {
            throw new InvoiceEmissionException(
                    "Erro de comunicação com o Focus NFe",
                    e.getMessage()
            );
        }
    }

    // monta o JSON exigido pelo Focus NFe para NFS-e em São Paulo
    private Map<String, Object> buildPayload(ServiceInvoice invoice, Emitter emitter, Client client) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("data_emissao", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        // prestador de serviços — dados do emitente
        Map<String, Object> prestador = new HashMap<>();
        prestador.put("cnpj", limparFormatacao(emitter.getCpfCnpj()));
        // inscrição municipal só é enviada se estiver preenchida — campo vazio causa rejeição
        if (emitter.getMunicipalRegistration() != null && !emitter.getMunicipalRegistration().isBlank()) {
            prestador.put("inscricao_municipal", limparFormatacao(emitter.getMunicipalRegistration()));
        }
        prestador.put("codigo_municipio", COD_MUNICIPIO_SAO_PAULO);
        payload.put("prestador", prestador);

        // tomador de serviços — dados do cliente
        Map<String, Object> tomador = new HashMap<>();
        tomador.put("razao_social", client.getName());
        tomador.put("email", client.getEmail() != null ? client.getEmail() : "");

        // CPF (11 dígitos) ou CNPJ (14 dígitos)
        String cpfCnpjLimpo = limparFormatacao(client.getCpfCnpj());
        if (cpfCnpjLimpo.length() == 14) {
            tomador.put("cnpj", cpfCnpjLimpo);
        } else {
            tomador.put("cpf", cpfCnpjLimpo);
        }

        // inscrição municipal do cliente (se tiver)
        if (client.getMunicipalRegistration() != null && !client.getMunicipalRegistration().isBlank()) {
            tomador.put("inscricao_municipal", limparFormatacao(client.getMunicipalRegistration()));
        }

        // endereço do cliente
        Map<String, Object> endereco = new HashMap<>();
        endereco.put("logradouro", client.getStreet() != null ? client.getStreet() : "");
        endereco.put("numero", client.getNumber() != null ? client.getNumber() : "S/N");
        endereco.put("complemento", client.getComplement() != null ? client.getComplement() : "");
        endereco.put("bairro", client.getNeighborhood() != null ? client.getNeighborhood() : "");
        endereco.put("codigo_municipio", COD_MUNICIPIO_SAO_PAULO);
        endereco.put("uf", client.getState() != null ? client.getState() : "SP");
        endereco.put("cep", limparFormatacao(client.getZipCode() != null ? client.getZipCode() : ""));
        tomador.put("endereco", endereco);
        payload.put("tomador", tomador);

        // dados do serviço prestado
        Map<String, Object> servico = new HashMap<>();
        servico.put("valor_servicos", invoice.getValue());
        servico.put("item_lista_servico", invoice.getServiceCode());
        servico.put("discriminacao", invoice.getServiceDescription());
        servico.put("municipio_prestacao_servico", COD_MUNICIPIO_SAO_PAULO);
        payload.put("servico", servico);

        return payload;
    }

    // interpreta a resposta do Focus NFe e preenche a ServiceInvoice
    private ServiceInvoice parseResponse(ServiceInvoice invoice, Map<String, Object> response) {
        if (response == null) return invoice;

        if (response.containsKey("numero")) {
            invoice.setInvoiceNumber(String.valueOf(response.get("numero")));
        }
        if (response.containsKey("codigo_verificacao")) {
            invoice.setVerificationCode(String.valueOf(response.get("codigo_verificacao")));
        }
        if (response.containsKey("url")) {
            invoice.setInvoiceUrl(String.valueOf(response.get("url")));
        }
        return invoice;
    }

    // remove pontos, traços, barras e espaços de CPF/CNPJ/CEP/IM
    private String limparFormatacao(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("[.\\-/\\s]", "");
    }
}


