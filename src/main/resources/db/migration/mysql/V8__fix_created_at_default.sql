-- V8__fix_created_at_default.sql (MySQL)
-- Ajusta NOT NULL + DEFAULT CURRENT_TIMESTAMP em patio.created_at

-- 1) Preenche valores nulos antes de forçar NOT NULL
UPDATE patio SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;

-- 2) Descobre o tipo atual da coluna (timestamp, datetime, datetime(6), etc.)
SET @coltype := (
  SELECT COLUMN_TYPE
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME   = 'patio'
    AND COLUMN_NAME  = 'created_at'
);

-- 3) Monta o ALTER mantendo o tipo atual, só garantindo NOT NULL + DEFAULT
--    Ex.: vira "ALTER TABLE patio MODIFY COLUMN created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP"
SET @ddl := CONCAT(
  'ALTER TABLE patio MODIFY COLUMN created_at ',
  @coltype,
  ' NOT NULL DEFAULT CURRENT_TIMESTAMP'
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
