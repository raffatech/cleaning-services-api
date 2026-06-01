# Agente de Codificação — POC Sistema de Serviços de Limpeza

## Quem sou eu (desenvolvedor)
- Sou uma desenvolvedora **júnior** aprendendo na prática
- Preciso que você **explique tudo antes de escrever código**
- Gosto de analogias simples para entender conceitos novos
- Estou aprendendo arquitetura hexagonal e API first pela primeira vez

## O que é esse projeto
POC (prova de conceito) pedida pelo meu Líder Técnico para aprender arquitetura hexagonal + API First na prática.

**O plano de desenvolvimento em ordem:**
1. ✅ Infra configurada (Podman + PostgreSQL + Spring Boot + Swagger)
2. 🚧 **CRUD de Funcionários** ← iniciamos daqui
3. ⏳ CRUD de Clientes
4. ⏳ Registro de Serviços de Limpeza
5. ⏳ Emissão de Recibo e Nota Fiscal

**Funcionário** é o ponto de partida porque é o mais simples:
tem apenas nome, CPF e cargo — ideal para aprender o fluxo completo.

**Stack tecnológica:**
- Java 21 + Spring Boot 3.4.5
- PostgreSQL (rodando via Podman em container)
- Lombok (para reduzir código repetitivo)
- Swagger/SpringDoc OpenAPI (documentação da API)
- Maven (gerenciador de dependências)

---

## Padrões obrigatórios (pedido pelo lider tecnico)

### 1. Arquitetura Hexagonal
O código é dividido em 3 grandes partes:

```
src/main/java/com/example/meuCrud/
├── application/
│   └── ports/
│       ├── inbound/
│       │   └── <Dominio>ServicePort.java    <- interface: define o que a app faz
│       └── outbound/
│           └── <Dominio>RepositoryPort.java <- interface: define o que a app precisa do banco
├── domain/
│   ├── model/
│   │   └── <Dominio>.java                  <- objeto puro, sem anotações Spring/JPA
│   ├── service/
│   │   └── <Dominio>Service.java           <- regras de negócio (@Service)
│   └── exception/
│       └── <Dominio>NotFoundException.java
└── infrastructure/
    ├── adapters/
    │   ├── inbound/
    │   │   └── web/
    │   │       └── <Dominio>ControllerAdapter.java  <- @RestController
    │   └── outbound/
    │       └── persistence/
    │           ├── <Dominio>RepositoryAdapter.java  <- implementação JPA
    │           ├── <Dominio>JpaRepository.java      <- interface Spring Data
    │           └── entity/
    │               └── <Dominio>Entity.java         <- @Entity (tabela no banco)
    └── config/
        └── GlobalExceptionHandler.java              <- trata erros de forma centralizada
```

### 2. API First
O contrato da API fica no arquivo `src/main/resources/openapi.yaml`, e o Swagger é gerado a partir disso.
Todo endpoint **deve ser documentado no YAML antes** de ser implementado em Java.

**Ciclo obrigatório para cada novo endpoint:**
```
1. Escrever o endpoint no openapi.yaml  (o contrato)
2. Confirmar como ficou no Swagger      (visualizar antes de codar)
3. Criar o controller Java              (implementar exatamente o que foi prometido)
```

**Regra de ouro:**
- ❌ Nunca criar um endpoint Java que não esteja descrito primeiro no `openapi.yaml`
- ✅ Todo novo endpoint começa no YAML — só depois vai pro Java

**Ao implementar o controller, usar:**
- `@Operation` para descrever o que o endpoint faz
- `@ApiResponse` para documentar os possíveis retornos
- Seguir REST: `GET` (buscar), `POST` (criar), `PUT` (atualizar), `DELETE` (remover)

**Quando eu pedir um novo endpoint, o agente deve:**
1. Mostrar o trecho YAML primeiro
2. Perguntar se está certo
3. Só então criar o controller Java correspondente

### 3. Regras de código obrigatórias
- **DTOs separados** das entidades JPA — nunca expor `@Entity` diretamente na API
- **Injeção via construtor** — nunca usar `@Autowired` em campo
- **Validações** com `@Valid`, `@NotNull`, `@NotBlank` nos DTOs de entrada
- O modelo de domínio (`domain/model/`) **não pode ter** anotações Spring ou JPA

### 4. Fluxo obrigatório de uma requisição
```
HTTP Request
  → ControllerAdapter      (recebe a requisição HTTP)
      → ServicePort        (interface — define o contrato)
          → Service        (implementa a regra de negócio)
              → RepositoryPort   (interface — define o que precisa do banco)
                  → RepositoryAdapter  (faz a operação no banco via JPA)
```

---

## Nomenclatura obrigatória

| Tipo | Padrão | Exemplo para Funcionário |
|---|---|---|
| Interface porta inbound | `<Dominio>ServicePort.java` | `FuncionarioServicePort.java` |
| Interface porta outbound | `<Dominio>RepositoryPort.java` | `FuncionarioRepositoryPort.java` |
| Serviço (regra de negócio) | `<Dominio>Service.java` | `FuncionarioService.java` |
| Controller (entrada HTTP) | `<Dominio>ControllerAdapter.java` | `FuncionarioControllerAdapter.java` |
| Adapter JPA (saída banco) | `<Dominio>RepositoryAdapter.java` | `FuncionarioRepositoryAdapter.java` |
| Interface Spring Data | `<Dominio>JpaRepository.java` | `FuncionarioJpaRepository.java` |
| Entidade JPA (tabela) | `<Dominio>Entity.java` | `FuncionarioEntity.java` |
| Modelo de domínio | `<Dominio>.java` | `Funcionario.java` |
| DTO de entrada | `<Dominio>Request.java` | `FuncionarioRequest.java` |
| DTO de saída | `<Dominio>Response.java` | `FuncionarioResponse.java` |

---

## Como você deve me ajudar

### SEMPRE faça isso antes de escrever qualquer código:
1. **Explique o que vai fazer** de forma clara, simples e em português
2. **Diga em qual camada** da arquitetura o código vai ficar e por quê
3. **Use uma analogia** se for um conceito novo
4. **Liste os arquivos** que serão criados ou modificados
5. **Após isso mostrar como vai ficar e SEMPRE perguntar se pode fazer**

### Ao escrever código:
- Coloque **comentários em português** explicando as partes importantes
- Explique o que cada anotação faz quando aparecer pela primeira vez (ex: `@Entity`, `@Service`)
- Depois de criar um endpoint, mostre **como testar no Swagger**

### Formato de resposta:
```
📌 O que vou fazer: [descrição simples]
🏗️ Onde fica na arquitetura: [camada + motivo]
💡 Analogia: [comparação simples, quando necessário]
📁 Arquivos que serão criados/modificados: [lista]
💻 Código: [código com comentários]
✅ Como testar no Swagger: [passo a passo]
```