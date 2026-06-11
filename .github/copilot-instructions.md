# Agente de Codificação — Sistema de Serviços de Limpeza

## Quem sou eu (desenvolvedora)
- Sou uma desenvolvedora **júnior** aprendendo na prática
- Preciso que você **explique tudo antes de escrever código**
- Gosto de analogias simples para entender conceitos novos
- Estou aprendendo arquitetura hexagonal e API First na prática

---

## O que é esse projeto

Sistema real de gestão para **prestadores de serviço de limpeza de vidros**.
Começou como POC (prova de conceito) do Líder Técnico, e virou produto real usado pelos clientes.

### Quem usa o sistema
| Usuário | Empresa | CPF/CNPJ |
|---|---|---|
| Igor Rodrigues dos Santos | Cleaner Rodrigues | 447.924.208-22 |
| Joel Saldanha de Cruz | Talento Vitrines | 353.204.882 |

### O que o sistema faz hoje
- **Recibo PDF** — comprovante de pagamento gerado na hora
- **Orçamento PDF** — proposta com itens, valores, descontos e formas de pagamento
- **NFS-e** — emissão de Nota Fiscal de Serviços via Focus NFe (mock e produção)
- **PWA** — app instalável no celular, sem precisar de loja de aplicativos

### Repositórios
- **GitLab (principal):** `https://gitlab.com/rafaela-gomes/cleaning-services-api`
- **GitHub (deploy/ngrok):** `https://github.com/raffatech/cleaning-services-api`

---

## Estado atual do desenvolvimento

```
✅ Infra configurada (Podman + PostgreSQL + Spring Boot + Swagger)
✅ CRUD de Funcionários (Employee)
✅ CRUD de Clientes (Client)
✅ CRUD de Emitentes (Emitter)
✅ Geração de Recibo PDF
✅ Geração de Orçamento PDF
✅ Emissão de NFS-e via Focus NFe (mock + prod)
✅ PWA com telas de Recibo e Orçamento
🚧 Tela de Nota Fiscal no PWA  ← próximo passo
⏳ Deploy no Railway (nuvem, 24h sem notebook ligado)
⏳ Tela de NF no PWA finalizada
```

---

## Stack tecnológica

**Backend:**
- Java 21 + Spring Boot 3.4.5
- PostgreSQL (local via Podman; produção via Railway)
- Lombok (reduz código repetitivo)
- Swagger/SpringDoc OpenAPI — documentação em `src/main/resources/static/openapi.yaml`
- Maven (gerenciador de dependências)
- iText / OpenPDF (geração de PDF)
- Focus NFe API (emissão de NFS-e)

**Frontend:**
- PWA (Progressive Web App) — HTML + CSS + JS puro em `src/main/resources/static/index.html`
- Service Worker (`service-worker.js`) — cache offline, versão atual: `cleaner-v2`
- Manifest (`manifest.json`) — instalação no celular

**Infraestrutura:**
- Podman Compose (banco local)
- Railway (deploy na nuvem)
- ngrok (exposição local para testes externos)
- `system.properties` — define Java 21 para o Railway
- `Procfile` — comando de start: `web: java -jar target/*.jar`

---

## Arquitetura Hexagonal

```
src/main/java/com/example/cleaningservices/
├── application/
│   └── ports/
│       ├── inbound/
│       │   └── <Dominio>ServicePort.java    ← interface: define o que a app faz
│       └── outbound/
│           ├── <Dominio>RepositoryPort.java ← interface: define o que precisa do banco
│           ├── <Dominio>PdfPort.java        ← interface: define geração de PDF
│           └── NfseGatewayPort.java         ← interface: define integração com Focus NFe
├── domain/
│   ├── model/
│   │   └── <Dominio>.java                  ← objeto puro, sem anotações Spring/JPA
│   ├── service/
│   │   └── <Dominio>Service.java           ← regras de negócio (@Service)
│   └── exception/
│       └── <Dominio>NotFoundException.java
└── infrastructure/
    ├── adapters/
    │   ├── inbound/
    │   │   └── web/
    │   │       ├── <Dominio>ControllerAdapter.java  ← @RestController
    │   │       ├── <Dominio>Request.java            ← DTO de entrada
    │   │       └── <Dominio>Response.java           ← DTO de saída
    │   └── outbound/
    │       ├── persistence/
    │       │   ├── <Dominio>RepositoryAdapter.java  ← implementação JPA
    │       │   ├── <Dominio>JpaRepository.java      ← interface Spring Data
    │       │   └── entity/
    │       │       └── <Dominio>Entity.java         ← @Entity (tabela no banco)
    │       ├── pdf/
    │       │   ├── PdfGeneratorAdapter.java         ← geração de recibo
    │       │   └── QuotePdfGeneratorAdapter.java    ← geração de orçamento
    │       └── nfse/
    │           ├── MockNfseAdapter.java             ← simulação (profile: mock)
    │           └── FocusNfeAdapter.java             ← produção (profile: prod)
    └── config/
        └── GlobalExceptionHandler.java              ← trata erros centralmente
```

### Fluxo de uma requisição
```
HTTP Request
  → ControllerAdapter      (recebe a requisição HTTP)
      → ServicePort        (interface — define o contrato)
          → Service        (implementa a regra de negócio)
              → RepositoryPort / PdfPort / NfseGatewayPort
                  → Adapter concreto (banco, PDF, Focus NFe)
```

---

## API First — regra de ouro

O contrato fica em `src/main/resources/static/openapi.yaml`.  
**Nunca criar endpoint Java sem antes documentar no YAML.**

**Ciclo obrigatório:**
```
1. Escrever o endpoint no openapi.yaml
2. Confirmar no Swagger (http://localhost:8080/swagger-ui.html)
3. Criar o controller Java correspondente
```

**Ao implementar:**
- `@Operation` para descrever o endpoint
- `@ApiResponse` para cada código de retorno
- REST semântico: GET (buscar), POST (criar), PUT (atualizar), DELETE (remover)

---

## Regras de código obrigatórias

- **DTOs separados** das entidades JPA — nunca expor `@Entity` diretamente na API
- **Injeção via construtor** — nunca usar `@Autowired` em campo
- **Validações** com `@Valid`, `@NotNull`, `@NotBlank` nos DTOs de entrada
- O modelo de domínio (`domain/model/`) **não pode ter** anotações Spring ou JPA
- **Comentários em português** em todo o código novo

---

## Nomenclatura obrigatória

| Tipo | Padrão | Exemplo |
|---|---|---|
| Interface porta inbound | `<Dominio>ServicePort.java` | `QuoteServicePort.java` |
| Interface porta outbound | `<Dominio>RepositoryPort.java` | `QuoteRepositoryPort.java` |
| Serviço (regra de negócio) | `<Dominio>Service.java` | `QuoteService.java` |
| Controller (entrada HTTP) | `<Dominio>ControllerAdapter.java` | `QuoteControllerAdapter.java` |
| Adapter JPA (saída banco) | `<Dominio>RepositoryAdapter.java` | `QuoteRepositoryAdapter.java` |
| Interface Spring Data | `<Dominio>JpaRepository.java` | `QuoteJpaRepository.java` |
| Entidade JPA (tabela) | `<Dominio>Entity.java` | `QuoteEntity.java` |
| Modelo de domínio | `<Dominio>.java` | `Quote.java` |
| DTO de entrada | `<Dominio>Request.java` | `QuoteRequest.java` |
| DTO de saída | `<Dominio>Response.java` | `QuoteResponse.java` |

---

## PWA — como funciona

O frontend é um arquivo único: `src/main/resources/static/index.html`

**Fluxo de telas:**
```
Tela 1 — Tipo: [📄 Recibo]  [📋 Orçamento]  [🧾 Nota Fiscal]
    ↓
Tela 2 — Emitente: [Igor / Cleaner Rodrigues]  [Joel / Talento Vitrines]
    ↓
Tela 3A — Formulário do Recibo (número, cliente, valor)
Tela 3B — Formulário do Orçamento (itens dinâmicos, desconto, pagamento)
Tela 3C — Formulário da NF (cliente via dropdown da API, descrição, valor)
```

**Variável de URL:** `const API_URL = ''` (vazia = relativo, funciona local e na nuvem)

**Service Worker:** versão `cleaner-v2` — sempre incrementar ao mudar o HTML

---

## Configuração local

```bash
# Subir banco
podman compose up -d

# Rodar a aplicação
mvn spring-boot:run

# Acessar
http://localhost:8080          ← PWA
http://localhost:8080/swagger-ui.html  ← Swagger

# Expor externamente (para testes no celular)
ngrok http 8080
```

**Perfis Spring:**
- `mock` — NFS-e simulada, sem token (padrão local)
- `prod` — NFS-e real via Focus NFe (exige `FOCUSNFE_TOKEN`)

---

## Como você deve me ajudar

### SEMPRE antes de escrever código:
1. **Explique o que vai fazer** em português claro e simples
2. **Diga em qual camada** da arquitetura o código vai ficar e por quê
3. **Use uma analogia** se for um conceito novo ou complexo
4. **Liste os arquivos** que serão criados ou modificados
5. **Pergunte se pode prosseguir** antes de gerar o código

### Ao escrever código:
- Comentários em **português** explicando as partes importantes
- Explique anotações novas quando aparecerem pela primeira vez
- Depois de criar um endpoint, mostre como testar no Swagger

### Formato de resposta:
```
📌 O que vou fazer: [descrição simples]
🏗️ Onde fica na arquitetura: [camada + motivo]
💡 Analogia: [quando necessário]
📁 Arquivos que serão criados/modificados: [lista]
💻 Código: [código com comentários]
✅ Como testar: [passo a passo]
```