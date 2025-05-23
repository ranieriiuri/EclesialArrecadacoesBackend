-- V1__create_schema.sql

-- EXTENSÃO PARA SUPORTE A UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- CRIAÇÃO DOS TIPOS DE ENUM
CREATE TYPE tipo_evento AS ENUM ('bazar', 'cantina', 'doacao', 'venda');

CREATE TYPE categoria_peca AS ENUM (
    'Roupa masculina adulta',
    'Roupa feminina adulta',
    'Roupa masculina infantil',
    'Roupa feminina infantil',
    'Calçado masculino adulto',
    'Calçado feminino adulto',
    'Calçado masculino infantil',
    'Calçado feminino infantil',
    'Acessório masculino adulto',
    'Acessório feminino adulto',
    'Acessório masculino infantil',
    'Acessório feminino infantil'
);

-- USUÁRIOS DO SISTEMA
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    mfa_secreto VARCHAR(255),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- DOADORES
CREATE TABLE doadores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    contato VARCHAR(100),
    observacoes TEXT
);

-- EVENTOS
CREATE TABLE eventos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tipo tipo_evento NOT NULL,
    descricao TEXT,
    data_inicio DATE, -- ao criar seta
    data_fim DATE,     -- ao "status (abaixo) = false", seta
    status BOOLEAN NOT NULL DEFAULT TRUE, -- TRUE = criado e ativo / FALSE = criado e finalizado
    criado_por UUID REFERENCES usuarios(id),
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
    doador_id UUID REFERENCES doadores(id),
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vendidas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    peca_id UUID REFERENCES pecas(id),
    evento_id UUID REFERENCES eventos(id),
    quantidade INT NOT NULL,
    comprador VARCHAR(100),
    valor_arrecadado DECIMAL(10,2) NOT NULL,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);