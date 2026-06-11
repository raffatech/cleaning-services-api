# PROMPT PRONTO — Cole isso no ChatGPT para retomar o projeto

---

Copie tudo abaixo desta linha e cole no ChatGPT:

---

Olá! Preciso que você me ajude a continuar o desenvolvimento de um projeto Java Spring Boot. Vou te dar todo o contexto necessário.

## SOBRE MIM
Sou desenvolvedora júnior aprendendo na prática. Preciso que você:
- Explique tudo antes de escrever código
- Diga em qual camada da arquitetura o código vai ficar
- Use analogias simples para conceitos novos
- Liste os arquivos que serão criados/modificados
- Sempre pergunte se pode fazer antes de codar
- Coloque comentários em português no código

## O QUE É O PROJETO
Sistema de gestão para prestadores de serviço de limpeza de vitrines.
É um produto genérico (SaaS) — não é só para dois clientes, qualquer prestador pode se cadastrar.

Primeiros usuários:
- Igor Rodrigues dos Santos — CPF 447.924.208-22 — Empresa: Cleaner Rodrigues
- Joel Saldanha de Cruz — CNPJ 53.721.210/0001-35 — Empresa: Talento Limpeza de Vitrine LTDA

Os 3 pilares:
1. Recibo PDF — JÁ FUNCIONA
2. Orçamento PDF — pendente
3. NFS-e (Nota Fiscal Eletrônica de Serviços de São Paulo) — EM IMPLEMENTAÇÃO

## STACK
- Java 21 + Spring Boot 3.4.5
- PostgreSQL
- Maven
- Swagger/OpenAPI
- Frontend: HTML + CSS + JS puro (PWA instalável no celular)

## ARQUITETURA HEXAGONAL
O código segue arquitetura hexagonal com essa estrutura:

```
src/main/java/com/example/cleaningservices/
├── application/ports/
│   ├── inbound/     ← interfaces: contrato do que a app faz
│   └── outbound/    ← interfaces: contrato do que a app precisa do mundo externo
├── domain/
│   ├── model/       ← objetos puros, SEM Spring ou JPA
│   ├── service/     ← regras de negócio (@Service)
│   └── exception/   ← exceções customizadas
└── infrastructure/
    ├── adapters/
    │   ├── inbound/web/        ← @RestController
    │   └── outbound/
    │       ├── persistence/    ← JPA + banco
    │       ├── pdf/            ← gerador PDF
    │       └── nfse/           ← (CRIAR) adapter Focus NFe
    └── config/
```

Fluxo obrigatório:
HTTP → ControllerAdapter → ServicePort → Service → RepositoryPort → RepositoryAdapter → Banco

## PADRÃO DE CÓDIGO (siga sempre este estilo)

### Exemplo de modelo de domínio:
```java
public class Emitter {
    private Long id;
    private String companyName;
    private String signerName;
    private String cpfCnpj;
    private String templatePath;
    // getters e setters sem Lombok
}
```

### Exemplo de porta inbound:
```java
public interface EmitterServicePort {
    Emitter createEmitter(Emitter emitter);
    List<Emitter> findAllEmitters();
    Emitter findEmitterById(Long id);
    Emitter updateEmitter(Long id, Emitter emitter);
    void deleteEmitter(Long id);
}
```

### Exemplo de serviço:
```java
@Service
public class EmitterService implements EmitterServicePort {
    private final EmitterRepositoryPort emitterRepositoryPort;
    public EmitterService(EmitterRepositoryPort emitterRepositoryPort) {
        this.emitterRepositoryPort = emitterRepositoryPort;
    }
    // implementação das regras de negócio
}
```

### Exemplo de entidade JPA:
```java
@Entity
@Table(name = "emitters")
public class EmitterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false, unique = true)
    private String cpfCnpj;
    // getters e setters
}
```

### Exemplo de controller:
```java
@RestController
@RequestMapping("/emitters")
@Tag(name = "Emitters", description = "Emitter management endpoints")
public class EmitterControllerAdapter {
    private final EmitterServicePort emitterServicePort;
    public EmitterControllerAdapter(EmitterServicePort emitterServicePort) {
        this.emitterServicePort = emitterServicePort;
    }
    @PostMapping
    @Operation(summary = "Create a new emitter")
    public ResponseEntity<EmitterResponse> createEmitter(@Valid @RequestBody EmitterRequest request) {
        // converte request → domínio → chama service → converte → retorna response
    }
}
```

## O QUE JÁ ESTÁ IMPLEMENTADO

✅ CRUD Funcionários (/employees)
✅ CRUD Emitentes (/emitters)
✅ CRUD Clientes (/clients) — com endereço estruturado (street, number, complement, neighborhood, city, state, zipCode, municipalRegistration)
✅ Geração de Recibo PDF (/receipts)
✅ Frontend PWA básico (index.html servido pelo Spring Boot)
✅ GlobalExceptionHandler
✅ CorsConfig
✅ openapi.yaml (API First)

## MODELO DO CLIENTE (recém implementado, para referência)
```java
public class Client {
    private Long id;
    private String name;
    private String cpfCnpj;
    private String email;
    private String phone;
    private String zipCode;      // CEP
    private String street;       // logradouro (ViaCEP preenche)
    private String number;       // número (usuário digita)
    private String complement;   // complemento (opcional)
    private String neighborhood; // bairro (ViaCEP preenche)
    private String city;         // cidade (ViaCEP preenche)
    private String state;        // UF (ViaCEP preenche)
    private String municipalRegistration; // inscrição municipal (para NFS-e)
}
```

## O QUE PRECISA SER IMPLEMENTADO (em ordem)

### PASSO 1 — Adicionar municipalRegistration no Emitter
Adicionar campo `municipalRegistration` (Inscrição Municipal) nos arquivos:
- Emitter.java (domain/model)
- EmitterEntity.java (persistence/entity)
- EmitterRepositoryAdapter.java (atualizar conversões)
- EmitterRequest.java (DTO entrada)
- EmitterResponse.java (DTO saída)
- EmitterControllerAdapter.java (atualizar toDomain/toResponse)
- openapi.yaml (schemas EmitterRequest e EmitterResponse)

### PASSO 2 — Adicionar dependência WebFlux no pom.xml
Para fazer chamadas HTTP ao Focus NFe:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

Adicionar no application.properties:
```properties
focusnfe.token=${FOCUSNFE_TOKEN}
focusnfe.ambiente=homologacao
focusnfe.url.homologacao=https://homologacao.focusnfe.com.br
focusnfe.url.producao=https://api.focusnfe.com.br
```

### PASSO 3 — Implementar NFS-e completa

Criar os seguintes arquivos:

**domain/model/ServiceInvoice.java**
Campos: id, status, emitterId, clientId, value, serviceDescription, serviceCode, cityOfService, issueDate, invoiceNumber, verificationCode, invoiceUrl, focusNfeReference

**domain/exception/InvoiceEmissionException.java**

**application/ports/inbound/ServiceInvoiceServicePort.java**
Método: ServiceInvoice emitInvoice(ServiceInvoice invoice)

**application/ports/outbound/NfseGatewayPort.java**
Método: ServiceInvoice emit(ServiceInvoice invoice, Emitter emitter, Client client)

**domain/service/ServiceInvoiceService.java**
- Busca Emitter pelo emitterId
- Busca Client pelo clientId
- Chama NfseGatewayPort.emit()

**infrastructure/adapters/outbound/nfse/FocusNfeAdapter.java**
Implementa NfseGatewayPort. Monta o JSON abaixo e faz POST via WebClient.

**infrastructure/adapters/inbound/web/ServiceInvoiceRequest.java**
Campos: emitterId, clientId, value, serviceDescription, serviceCode (default "01406"), cityOfService (default "São Paulo")

**infrastructure/adapters/inbound/web/ServiceInvoiceResponse.java**
Campos: invoiceNumber, verificationCode, status, invoiceUrl, issueDate

**infrastructure/adapters/inbound/web/ServiceInvoiceControllerAdapter.java**
Endpoint: POST /invoices

## PAYLOAD FOCUS NFe — NFS-e SÃO PAULO

Endpoint: POST /v2/nfse?ref={referencia_unica}
Autenticação: HTTP Basic Auth (token como usuário, senha vazia)

```json
{
  "data_emissao": "2026-06-11",
  "prestador": {
    "cnpj": "53721210000135",
    "inscricao_municipal": "79688888",
    "codigo_municipio": "3550308"
  },
  "tomador": {
    "cnpj": "05828732019450",
    "razao_social": "VMT TELECOMUNICACOES LTDA",
    "email": "",
    "endereco": {
      "logradouro": "R Domingos de Morais",
      "numero": "2584",
      "complemento": "Loja EC SMST 0033",
      "bairro": "Vila Mariana",
      "codigo_municipio": "3550308",
      "uf": "SP",
      "cep": "04036100"
    }
  },
  "servico": {
    "valor_servicos": 90.00,
    "item_lista_servico": "01406",
    "discriminacao": "Limpeza de vitrine",
    "municipio_prestacao_servico": "3550308"
  }
}
```

Código município São Paulo (IBGE): 3550308
Código serviço limpeza: 01406
Regime tributário Joel: Simples Nacional

### PASSO 4 — PWA (app instalável no celular)

Criar em src/main/resources/static/:
- manifest.json (nome "Cleaner Rodrigues", cor #0d1b2a, ícones)
- service-worker.js (cache básico para offline)
- icons/icon-192.png e icons/icon-512.png

Adicionar no index.html:
```html
<link rel="manifest" href="/manifest.json">
<meta name="theme-color" content="#0d1b2a">
<link rel="apple-touch-icon" href="/icons/icon-192.png">
<meta name="apple-mobile-web-app-capable" content="yes">
```

## DECISÕES JÁ TOMADAS (não questione)
- Provedor NFS-e: Focus NFe (Nuvem Fiscal encerrou em 31/07/2026)
- Frontend: PWA com HTML+CSS+JS puro (sem React)
- Endereço: campos separados (street, number, etc.) para compatibilidade com NFS-e e ViaCEP
- Arquitetura: hexagonal (não mudar)
- Injeção: sempre via construtor, nunca @Autowired
- DTOs: sempre separados das entidades JPA
- Comentários: sempre em português

## ONDE PAROU
O próximo passo é o PASSO 1 — adicionar municipalRegistration no Emitter.
A desenvolvedora está criando conta no Focus NFe em paralelo.

