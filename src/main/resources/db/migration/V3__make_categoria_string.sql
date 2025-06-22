-- V2__make_categoria_string.sql

-- Alterar a coluna categoria para varchar padrão
ALTER TABLE pecas
  ALTER COLUMN categoria TYPE VARCHAR(100);

-- (Opcional) Adicionar restrição de não-nulo
ALTER TABLE pecas
  ALTER COLUMN categoria SET NOT NULL;

-- Remove o tipo ENUM do PostgreSQL (se não for mais usado por nenhuma outra tabela)
DROP TYPE IF EXISTS categoria_peca;