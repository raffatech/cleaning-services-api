# 📋 CONTEXT — Cleaning Services API
> Documento criado em 2026-06-11 para continuidade do desenvolvimento.
> Se você é um novo agente de IA lendo isso: leia tudo antes de codar qualquer coisa.

---

## 👩‍💻 Sobre a desenvolvedora
- Desenvolvedora júnior aprendendo na prática
- Está aprendendo arquitetura hexagonal e API First pela primeira vez
- **Sempre explique antes de codar. Liste os arquivos. Pergunte se pode fazer.**
- Use comentários em português no código
- Siga o formato de resposta do arquivo `.github/copilot-instructions.md`

---

## 🏢 O que é esse sistema
Sistema de gestão para **prestadores de serviço de limpeza de vitrines**.
Não é exclusivo para dois clientes — é um **produto genérico** que pode atender múltiplos prestadores.

**Primeiros usuários:**
- **Igor Rodrigues dos Santos** — CPF: 447.924.208-22 — Empresa: Cleaner Rodrigues
- **Joel Saldanha de Cruz** — CNPJ: 53.721.210/0001-35 — Empresa: Talento Limpeza de Vitrine LTDA

**Os 3 pilares do sistema:**
1. **Recibo PDF** ✅ já funciona
2. **Orçamento PDF** ⏳ estrutura pendente
3. **NFS-e (Nota Fiscal Eletrônica de Serviços)** 🚧 em implementação

---

## 🛠️ Stack tecnológica
- Java 21 + Spring Boot 3.4.5
- PostgreSQL (rodando via Docker/Podman em container)
- Swagger/SpringDoc OpenAPI (documentação da API)
- Maven (gerenciador de dependências)
- Frontend: HTML + CSS + JS puro (PWA — instalável no celular)
- Hospedagem atual: local + ngrok para expor externamente

---

## 🏗️ Arquitetura Hexagonal — estrutura de pastas

```
src/main/java/com/example/cleaningservices/
├── application/
│   └── ports/
│       ├── inbound/           ← interfaces: o que a app faz
│       └── outbound/          ← interfaces: o que a app precisa do mundo externo
├── domain/
│   ├── model/                 ← objetos puros, SEM anotações Spring ou JPA
│   ├── service/               ← regras de negócio (@Service)
│   └── exception/             ← exceções customizadas
└── infrastructure/
    ├── adapters/
    │   ├── inbound/web/       ← @RestController (entrada HTTP)
    │   └── outbound/
    │       ├── persistence/   ← JPA (banco de dados)
    │       ├── pdf/           ← gerador de PDF
    │       └── nfse/          ← ⏳ CRIAR: adapter para Focus NFe
    └── config/                ← configurações globais
```

**Fluxo obrigatório de uma requisição:**
```
HTTP → ControllerAdapter → ServicePort → Service → RepositoryPort → RepositoryAdapter → Banco
```

---

## ✅ O que já está implementado e funcionando

### CRUD de Funcionários (`/employees`)
- `EmployeeServicePort`, `EmployeeRepositoryPort`
- `EmployeeService`, `EmployeeJpaRepository`, `EmployeeRepositoryAdapter`
- `EmployeeEntity`, `Employee` (domínio), `EmployeeRole` (enum)
- `EmployeeControllerAdapter`, `EmployeeRequest`, `EmployeeResponse`
- `EmployeeNotFoundException`, `EmployeeValidationException`

### CRUD de Emitentes (`/emitters`)
- `EmitterServicePort`, `EmitterRepositoryPort`
- `EmitterService`, `EmitterJpaRepository`, `EmitterRepositoryAdapter`
- `EmitterEntity`, `Emitter` (domínio)
- `EmitterControllerAdapter`, `EmitterRequest`, `EmitterResponse`
- `EmitterNotFoundException`
- ⚠️ **FALTA adicionar:** campo `municipalRegistration` (Inscrição Municipal) — necessário para NFS-e

### CRUD de Clientes (`/clients`) — ✅ recém implementado
- `ClientServicePort`, `ClientRepositoryPort`
- `ClientService`, `ClientJpaRepository`, `ClientRepositoryAdapter`
- `ClientEntity`, `Client` (domínio)
- `ClientControllerAdapter`, `ClientRequest`, `ClientResponse`
- `ClientNotFoundException`
- **Endereço estruturado** (zipCode, street, number, complement, neighborhood, city, state)
- Campo `municipalRegistration` (Inscrição Municipal do cliente — para NFS-e)

### Geração de Recibo PDF (`/receipts`)
- `ReceiptServicePort`
- `ReceiptService`, `PdfGeneratorAdapter`, `NumberToWordsConverter`
- `Receipt` (domínio)
- `ReceiptControllerAdapter`, `ReceiptRequest`
- Templates PDF em: `src/main/resources/static/templates/`
  - `template-cleaner-rodrigues.pdf` (Igor)
  - `template-talento-vitrines.pdf` (Joel)

### Infraestrutura
- `GlobalExceptionHandler` — trata erros centralizadamente
- `CorsConfig` — permite chamadas do frontend
- `openapi.yaml` — contrato da API (API First)
- `src/main/resources/static/index.html` — frontend PWA (tela de gerar recibo)

---

## 🚧 O que está pendente de implementação

### PASSO 1 — Adicionar `municipalRegistration` no Emitter
**Arquivos a modificar:**
- `domain/model/Emitter.java` — adicionar campo
- `infrastructure/.../entity/EmitterEntity.java` — adicionar coluna
- `infrastructure/.../EmitterRepositoryAdapter.java` — atualizar conversões
- `infrastructure/.../web/EmitterRequest.java` — adicionar campo
- `infrastructure/.../web/EmitterResponse.java` — adicionar campo
- `infrastructure/.../web/EmitterControllerAdapter.java` — atualizar conversão
- `openapi.yaml` — atualizar schemas

**Por que:** A Inscrição Municipal do Emitente é obrigatória no payload da NFS-e.

---

### PASSO 2 — Adicionar dependência do Focus NFe no pom.xml
O Focus NFe usa **REST + JSON simples** — não tem SDK Java oficial.
Adicionar ao `pom.xml`:
```xml
<!-- Cliente HTTP para chamar a API do Focus NFe -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```
Adicionar ao `application.properties`:
```properties
focusnfe.token=${FOCUSNFE_TOKEN}
focusnfe.ambiente=homologacao
focusnfe.url.homologacao=https://homologacao.focusnfe.com.br
focusnfe.url.producao=https://api.focusnfe.com.br
```

---

### PASSO 3 — Implementar NFS-e (estrutura hexagonal completa)

**Arquivos a CRIAR:**

```
domain/model/ServiceInvoice.java
  Campos obrigatórios para NFS-e SP:
  - Long id
  - String status (PENDENTE, EMITIDA, CANCELADA, ERRO)
  - Long emitterId (referência ao Emitter)
  - Long clientId (referência ao Client)
  - BigDecimal value (valor do serviço)
  - String serviceDescription (discriminação — ex: "Limpeza de vitrine")
  - String serviceCode (código municipal — ex: "01406")
  - String cityOfService (município da prestação — ex: "São Paulo")
  - LocalDate issueDate (data de emissão)
  - String invoiceNumber (número da NF retornado pelo Focus NFe)
  - String verificationCode (código de verificação retornado)
  - String invoiceUrl (link para consulta)
  - String focusNfeReference (referência interna do Focus NFe)

domain/exception/InvoiceEmissionException.java

application/ports/inbound/ServiceInvoiceServicePort.java
  - ServiceInvoice emitInvoice(ServiceInvoice invoice)

application/ports/outbound/NfseGatewayPort.java
  ← PORTA para o Focus NFe (implementação fica na infra)
  - ServiceInvoice emit(ServiceInvoice invoice, Emitter emitter, Client client)

domain/service/ServiceInvoiceService.java
  - Busca Emitter pelo emitterId
  - Busca Client pelo clientId
  - Chama NfseGatewayPort.emit()
  - Retorna ServiceInvoice com número e código preenchidos

infrastructure/adapters/outbound/nfse/FocusNfeAdapter.java
  ← Implementa NfseGatewayPort
  ← Monta o JSON do Focus NFe e faz POST via WebClient/RestTemplate
  ← Trata erros da API externa

infrastructure/adapters/inbound/web/ServiceInvoiceRequest.java
  - Long emitterId
  - Long clientId (usa os dados salvos no banco — não precisa redigitar)
  - BigDecimal value
  - String serviceDescription (ex: "Limpeza de vitrine")
  - String serviceCode (opcional — padrão "01406")
  - String cityOfService (opcional — padrão "São Paulo")

infrastructure/adapters/inbound/web/ServiceInvoiceResponse.java
  - Integer invoiceNumber
  - String verificationCode
  - String status
  - String invoiceUrl
  - LocalDate issueDate

infrastructure/adapters/inbound/web/ServiceInvoiceControllerAdapter.java
  - POST /invoices
```

---

### PASSO 4 — PWA (Progressive Web App)
Transformar o `index.html` existente em app instalável no celular.

**Arquivos a CRIAR em `src/main/resources/static/`:**
```
manifest.json          ← nome, cor, ícones do "app"
service-worker.js      ← permite instalar no celular e funcionar offline
icons/
  icon-192.png         ← ícone 192x192px (iniciais "CR" fundo azul #4aa3df)
  icon-512.png         ← ícone 512x512px
```

**Modificar `index.html`:**
```html
<!-- Adicionar no <head>: -->
<link rel="manifest" href="/manifest.json">
<meta name="theme-color" content="#0d1b2a">
<link rel="apple-touch-icon" href="/icons/icon-192.png">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-title" content="Cleaner Rodrigues">

<!-- Adicionar no final do <body>: -->
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/service-worker.js');
  }
</script>
```

**Como instalar no celular após implementado:**
- Android: Chrome → menu ⋮ → "Adicionar à tela inicial"
- iPhone: Safari → botão compartilhar → "Adicionar à Tela de Início"

---

## 🔑 Decisões importantes já tomadas

| Decisão | Escolha | Motivo |
|---|---|---|
| Provedor NFS-e | **Focus NFe** | Nuvem Fiscal encerra em 31/07/2026; Focus NFe tem 30 dias grátis, R$89,90/mês depois |
| Frontend | **PWA (HTML+CSS+JS)** | Não precisa de React, funciona em Android e iOS, já temos o index.html |
| Endereço do Cliente | **Campos separados** (street, number, etc.) | NFS-e exige campos separados; ViaCEP preenche automaticamente pelo CEP |
| Arquitetura | **Hexagonal** | Isolamento entre camadas, fácil trocar provedores (ex: mudar Focus NFe → outro) |
| Certificado Digital | **Não necessário agora** | Focus NFe usa autenticação por token; certificado só para integração direta |

---

## 📊 Payload Focus NFe — NFS-e São Paulo

O Focus NFe recebe um POST em `/v2/nfse?ref=REFERENCIA` com este JSON:

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

**Código do município de São Paulo:** `3550308` (IBGE)
**Código do serviço de limpeza:** `01406`

**Autenticação Focus NFe:** HTTP Basic Auth com token no usuário e senha vazia:
```
Authorization: Basic BASE64(token:)
```

---

## 🌐 Endpoints da API (estado atual)

| Método | Rota | Status |
|---|---|---|
| POST | `/employees` | ✅ |
| GET | `/employees` | ✅ |
| GET | `/employees/{id}` | ✅ |
| PUT | `/employees/{id}` | ✅ |
| DELETE | `/employees/{id}` | ✅ |
| POST | `/emitters` | ✅ |
| GET | `/emitters` | ✅ |
| GET | `/emitters/{id}` | ✅ |
| PUT | `/emitters/{id}` | ✅ |
| DELETE | `/emitters/{id}` | ✅ |
| POST | `/clients` | ✅ |
| GET | `/clients` | ✅ |
| GET | `/clients/{id}` | ✅ |
| PUT | `/clients/{id}` | ✅ |
| DELETE | `/clients/{id}` | ✅ |
| POST | `/receipts` | ✅ |
| POST | `/invoices` | ⏳ pendente |

---

## ▶️ Como rodar o projeto localmente

```powershell
# 1. Subir o banco PostgreSQL
docker-compose up -d

# 2. Rodar a API
./mvnw spring-boot:run

# 3. Acessar o app
http://localhost:8080

# 4. Acessar o Swagger
http://localhost:8080/swagger-ui.html

# 5. Expor externamente (para testar no celular)
ngrok http 8080
```

---

## 📁 Nomenclatura obrigatória dos arquivos

| Tipo | Padrão |
|---|---|
| Interface porta inbound | `<Dominio>ServicePort.java` |
| Interface porta outbound | `<Dominio>RepositoryPort.java` |
| Serviço | `<Dominio>Service.java` |
| Controller | `<Dominio>ControllerAdapter.java` |
| Adapter JPA | `<Dominio>RepositoryAdapter.java` |
| Interface Spring Data | `<Dominio>JpaRepository.java` |
| Entidade JPA | `<Dominio>Entity.java` |
| Modelo de domínio | `<Dominio>.java` |
| DTO entrada | `<Dominio>Request.java` |
| DTO saída | `<Dominio>Response.java` |

---

*Última atualização: 2026-06-11*

