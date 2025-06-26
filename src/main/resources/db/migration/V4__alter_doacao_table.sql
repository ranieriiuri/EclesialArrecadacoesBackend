-- 1. Remove a foreign key (se existir)
ALTER TABLE doacoes DROP CONSTRAINT IF EXISTS doacoes_peca_id_fkey;

-- 2. Adiciona a nova coluna permitindo valores nulos inicialmente
ALTER TABLE doacoes ADD COLUMN nome_peca VARCHAR(255);

-- 3. Popula a nova coluna com os nomes das peças relacionadas
UPDATE doacoes
SET nome_peca = p.nome
FROM pecas p
WHERE doacoes.peca_id = p.id;

-- 4. Só então remove a coluna peca_id
ALTER TABLE doacoes DROP COLUMN IF EXISTS peca_id;

-- 5. E aplica a restrição NOT NULL
ALTER TABLE doacoes ALTER COLUMN nome_peca SET NOT NULL;
