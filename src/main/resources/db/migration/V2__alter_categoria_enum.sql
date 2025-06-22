-- V2__alter_categoria_enum.sql

-- 🔸 Remover a coluna que usa o tipo antigo
ALTER TABLE pecas DROP COLUMN categoria;

-- 🔸 Dropar o tipo antigo
DROP TYPE IF EXISTS categoria_peca;

-- 🔸 Criar o novo tipo com nomes do enum Java
CREATE TYPE categoria_peca AS ENUM (
    'ROUPA_MASCULINA_ADULTA',
    'ROUPA_FEMININA_ADULTA',
    'ROUPA_MASCULINA_INFANTIL',
    'ROUPA_FEMININA_INFANTIL',
    'CALCADO_MASCULINO_ADULTO',
    'CALCADO_FEMININO_ADULTO',
    'CALCADO_MASCULINO_INFANTIL',
    'CALCADO_FEMININO_INFANTIL',
    'ACESSORIO_MASCULINO_ADULTO',
    'ACESSORIO_FEMININO_ADULTO',
    'ACESSORIO_MASCULINO_INFANTIL',
    'ACESSORIO_FEMININO_INFANTIL'
);

-- 🔸 Adicionar novamente a coluna com o novo tipo
ALTER TABLE pecas ADD COLUMN categoria categoria_peca NOT NULL;
