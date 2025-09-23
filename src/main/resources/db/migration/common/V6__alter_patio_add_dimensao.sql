-- V6__alter_patio_add_dimensao.sql
-- Adiciona colunas de dimensão (largura/altura) ao pátio para o app mobile

-- 1) Adiciona as colunas individualmente (H2 lida melhor assim)
ALTER TABLE patio ADD COLUMN IF NOT EXISTS largura INTEGER;
ALTER TABLE patio ADD COLUMN IF NOT EXISTS altura  INTEGER;

-- 2) Preenche valores para registros existentes
UPDATE patio SET largura = 15 WHERE largura IS NULL;
UPDATE patio SET altura  = 15 WHERE altura  IS NULL;

-- 3) Define DEFAULT e NOT NULL
ALTER TABLE patio ALTER COLUMN largura SET DEFAULT 15;
ALTER TABLE patio ALTER COLUMN altura  SET DEFAULT 15;

ALTER TABLE patio ALTER COLUMN largura SET NOT NULL;
ALTER TABLE patio ALTER COLUMN altura  SET NOT NULL;

-- 4) (Opcional) Restrição de integridade
ALTER TABLE patio ADD CONSTRAINT patio_largura_altura_positive_ck
CHECK (largura > 0 AND altura > 0);
