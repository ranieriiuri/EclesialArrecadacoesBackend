-- V1__create_schema.sql

-- EXTENSÃO PARA SUPORTE A UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ENUMS
CREATE TYPE tipo_evento AS ENUM ('bazar', 'cantina', 'doacao', 'venda externa');

CREATE TYPE status_evento AS ENUM ('planejando', 'em andamento', 'finalizado');

CREATE TYPE categoria_peca AS ENUM (
    'Roupa masculina adulta',
    'Roupa feminina adulta',
    'Roupa masculina infantil',
    'Roupa feminina infantil',
    'Calcado masculino adulto',
    'Calcado feminino adulto',
    'Calcado masculino infantil',
    'Calcado feminino infantil',
    'Acessorio masculino adulto',
    'Acessorio feminino adulto',
    'Acessorio masculino infantil',
    'Acessorio feminino infantil'
);

-- IGREJAS (MULTI-TENANCY)
CREATE TABLE igrejas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    cidade VARCHAR(100),
    estado VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ENDEREÇOS (normalização para usuários)
CREATE TABLE enderecos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cep VARCHAR(20),
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    pais VARCHAR(50) NOT NULL DEFAULT 'Brasil'
);

-- USUÁRIOS
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    mfa_secreto VARCHAR(255),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cargo VARCHAR(100),
    endereco_id UUID REFERENCES enderecos(id),
    foto_perfil VARCHAR(255), -- para possivel foto de perfil
    igreja_id UUID REFERENCES igrejas(id), -- Multitenancy
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- DOADORES
CREATE TABLE doadores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    contato VARCHAR(100),
    observacoes TEXT,
    igreja_id UUID REFERENCES igrejas(id) -- Multitenancy
);

-- EVENTOS
CREATE TABLE eventos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tipo tipo_evento NOT NULL,
    descricao TEXT,
    data_inicio DATE,
    data_fim DATE,
    status status_evento NOT NULL DEFAULT 'planejando',
    criado_por UUID REFERENCES usuarios(id),
    igreja_id UUID REFERENCES igrejas(id), -- Multitenancy
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PEÇAS DO BAZAR
CREATE TABLE pecas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    cor VARCHAR(50),
    categoria categoria_peca NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,
    igreja_id UUID REFERENCES igrejas(id), -- Multitenancy
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- REGISTRO DE DOACOES - RELACIONANDO PEÇA AO DOADOR DA OCASIAO
CREATE TABLE doacoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    peca_id UUID NOT NULL REFERENCES pecas(id),
    doador_id UUID NOT NULL REFERENCES doadores(id),
    quantidade INT NOT NULL,
    data_doacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    igreja_id UUID REFERENCES igrejas(id) -- multitenancy, controle por igreja
);

-- REGISTRO DE PEÇAS VENDIDAS
CREATE TABLE vendas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    peca_id UUID REFERENCES pecas(id),
    evento_id UUID REFERENCES eventos(id),
    quantidade INT NOT NULL,
    comprador VARCHAR(100),
    valor_arrecadado DECIMAL(10,2) NOT NULL,
    igreja_id UUID REFERENCES igrejas(id), -- Multitenancy
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABELA UTILIZADA PARA GERAR TOKEN (TEMPORARIO) PARA RECUPERAR SENHA
CREATE TABLE tokens_recuperacao_senha (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id UUID NOT NULL,
    expira_em TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
