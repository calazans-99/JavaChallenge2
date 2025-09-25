SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME   = 'patio'
    AND COLUMN_NAME  = 'largura'
);

SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE patio ADD COLUMN largura INT',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) Adiciona coluna 'altura' se não existir
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME   = 'patio'
    AND COLUMN_NAME  = 'altura'
);

SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE patio ADD COLUMN altura INT',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3) Preenche valores existentes nulos
UPDATE patio SET largura = 15 WHERE largura IS NULL;
UPDATE patio SET altura  = 15 WHERE altura  IS NULL;

-- 4) NOT NULL + DEFAULT (em MySQL é via MODIFY)
ALTER TABLE patio
  MODIFY COLUMN largura INT NOT NULL DEFAULT 15,
  MODIFY COLUMN altura  INT NOT NULL DEFAULT 15;

-- 5) CHECK constraint só se ainda não existir
SET @ck_exists := (
  SELECT COUNT(*)
  FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'patio'
    AND CONSTRAINT_NAME = 'patio_largura_altura_positive_ck'
);

SET @ddl := IF(
  @ck_exists = 0,
  'ALTER TABLE patio ADD CONSTRAINT patio_largura_altura_positive_ck CHECK (largura > 0 AND altura > 0)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;
