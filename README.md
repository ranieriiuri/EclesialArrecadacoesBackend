# Eclesial API - Backend

Este é o backend do sistema Eclesial, desenvolvido com **Java + Spring Boot**, responsável pela gestão de usuários, igrejas, peças de roupa, doações, eventos e vendas.

## 🛠 Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security + JWT
- Hibernate Validator
- Cloudinary (upload de imagens)
- Banco de dados relacional (PostgreSQL)
- Lombok
- Swagger/OpenAPI
- Flyway (para gerar migrations)

## 📦 Funcionalidades

### 🔐 Autenticação
- Registro de usuário (com dados pessoais, igreja e endereço)
- Login com geração de JWT
- Atualização de senha
- Perfil com foto (upload para Cloudinary)

### 👤 Usuário
- Buscar dados completos do usuário logado
- Atualizar dados pessoais e endereço
- Atualizar foto de perfil
- Deletar conta
- Buscar por ID e listar todos (admin restrito)

### 🏛 Igreja
- Registro de igreja (associada a usuários, single-tenant)

### 📦 Peças & Doações
- Cadastro de peça com doador e doação simultaneamente
- Listagem de peças (todas, por categoria, por status)
- Atualização e exclusão de peças
- Controle de estoque

### 📆 Eventos
- Cadastro e listagem de eventos
- Iniciar e finalizar eventos
- Listar peças disponíveis em evento

### 💰 Vendas
- Registro de venda de peça durante o evento
- Cálculo automático de valor arrecadado
- Atualização de estoque da peça

### 📈 Relatórios
- Relatórios de vendas por evento ou período
- Totais arrecadados, quantidade de peças vendidas

## 🔗 Endpoints

Acesse a documentação Swagger em:
https://localhost:8443/swagger-ui/index.html

## 🔧 Configuração
Configure as variáveis no application.properties:

- Banco de dados
- JWT Secret
- Cloudinary API keys

Compile e rode:

```bash
./mvnw spring-boot:run
```

## 📌 Observações
- Upload de imagem feito via multipart/form-data para o Cloudinary
- Apenas usuários autenticados podem acessar endpoints protegidos (apenas /auth/login e /auth/register são privados)
- Validações feitas com anotações @Valid
