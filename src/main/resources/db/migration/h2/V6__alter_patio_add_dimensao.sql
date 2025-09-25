-- 1) Adiciona colunas se não existirem (H2 suporta IF NOT EXISTS)
ALTER TABLE patio ADD COLUMN IF NOT EXISTS largura INT;
ALTER TABLE patio ADD COLUMN IF NOT EXISTS altura  INT;

-- 2) Preenche valores existentes
UPDATE patio SET largura = 15 WHERE largura IS NULL;
UPDATE patio SET altura  = 15 WHERE altura  IS NULL;

-- 3) Defaults (H2: ALTER COLUMN SET/DROP DEFAULT)
ALTER TABLE patio ALTER COLUMN largura DROP DEFAULT;
ALTER TABLE patio ALTER COLUMN largura SET DEFAULT 15;

ALTER TABLE patio ALTER COLUMN altura DROP DEFAULT;
ALTER TABLE patio ALTER COLUMN altura SET DEFAULT 15;

-- 4) NOT NULL
ALTER TABLE patio ALTER COLUMN largura SET NOT NULL;
ALTER TABLE patio ALTER COLUMN altura  SET NOT NULL;

-- 5) CHECK constraint (idempotente não é suportado nativamente; como workaround,
--    tente criar e ignore se já existir. Em dev/H2, recriações são comuns.)
--    Se preferir segurança total, comente a linha abaixo quando já existir.
ALTER TABLE patio ADD CONSTRAINT patio_largura_altura_positive_ck CHECK (largura > 0 AND altura > 0);
