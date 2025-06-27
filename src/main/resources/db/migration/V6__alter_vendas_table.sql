-- 1. Adiciona coluna peca_nome
ALTER TABLE vendas
ADD COLUMN peca_nome VARCHAR(100);

-- 2. (Opcional) Popula peca_nome com base na tabela pecas
UPDATE vendas v
SET peca_nome = p.nome
FROM pecas p
WHERE v.peca_id = p.id;

-- 3. Remove FK e a coluna peca_id
ALTER TABLE vendas DROP CONSTRAINT vendas_peca_id_fkey;
ALTER TABLE vendas DROP COLUMN peca_id;
