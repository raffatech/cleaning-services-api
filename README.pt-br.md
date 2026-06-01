# 🧹 Cleaning Services API

🇺🇸 [Read in English](README.md)

API REST para gerenciamento de funcionários de uma empresa de serviços de limpeza.

Projeto desenvolvido como POC (Prova de Conceito) para aprendizado de **Arquitetura Hexagonal** e **API First** com Java + Spring Boot.

---

## 🏗️ Arquitetura

O projeto utiliza **Arquitetura Hexagonal (Ports and Adapters)**, que isola as regras de negócio do domínio de detalhes externos como banco de dados e HTTP.

```
src/main/java/com/example/cleaningservices/
├── application/
│   └── ports/
│       ├── inbound/   ← contratos do que a aplicação faz
│       └── outbound/  ← contratos do que a aplicação precisa
├── domain/
│   ├── model/         ← entidades de negócio (sem Spring/JPA)
│   ├── service/       ← regras de negócio
│   └── exception/     ← exceções do domínio
└── infrastructure/
    ├── adapters/
    │   ├── inbound/   ← controllers REST
    │   └── outbound/  ← adapters JPA (banco de dados)
    └── config/        ← tratamento global de erros
```

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| PostgreSQL | 16 |
| Lombok | - |
| SpringDoc OpenAPI (Swagger) | 2.8.9 |
| Maven | - |
| Podman | - |

---

## ▶️ Como rodar localmente

### Pré-requisitos
- Java 21
- Maven
- Podman (ou Docker)

### 1. Suba o banco de dados
```bash
podman compose up -d
```

### 2. Rode a aplicação
```bash
mvn spring-boot:run
```

### 3. Acesse o Swagger
```
http://localhost:8080/swagger-ui.html
```

---

## 📋 Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/employees` | Cadastra um novo funcionário |
| `GET` | `/employees` | Lista todos os funcionários |
| `GET` | `/employees/{id}` | Busca funcionário pelo ID |
| `PUT` | `/employees/{id}` | Atualiza dados de um funcionário |
| `DELETE` | `/employees/{id}` | Remove um funcionário |

### Roles disponíveis
| Valor | Descrição |
|---|---|
| `CLEANER` | Limpador(a) |
| `SUPERVISOR` | Supervisor(a) |
| `ADMIN` | Administrador(a) |

---

## 📦 Exemplo de requisição

**POST /employees**
```json
{
  "name": "Maria Silva",
  "cpf": "123.456.789-00",
  "role": "CLEANER",
  "email": "maria.silva@email.com"
}
```

**Resposta 201:**
```json
{
  "id": 1,
  "name": "Maria Silva",
  "cpf": "123.456.789-00",
  "role": "CLEANER",
  "email": "maria.silva@email.com"
}
```

---

## 👩‍💻 Autora

Desenvolvido por **Rafaela Gomes** como POC de aprendizado.

