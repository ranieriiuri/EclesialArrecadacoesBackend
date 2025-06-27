-- 1. Remover constraints de tipos ENUM
ALTER TABLE eventos ALTER COLUMN tipo DROP DEFAULT;
ALTER TABLE eventos ALTER COLUMN status DROP DEFAULT;

-- 2. Alterar colunas de ENUM para TEXT
ALTER TABLE eventos ALTER COLUMN tipo TYPE TEXT USING tipo::TEXT;
ALTER TABLE eventos ALTER COLUMN status TYPE TEXT USING status::TEXT;

-- 3. Remover os tipos ENUM do banco de dados
DROP TYPE IF EXISTS tipo_evento;
DROP TYPE IF EXISTS status_evento;

-- 4. (Opcional) Setar novos defaults como strings
ALTER TABLE eventos ALTER COLUMN status SET DEFAULT 'planejando';
