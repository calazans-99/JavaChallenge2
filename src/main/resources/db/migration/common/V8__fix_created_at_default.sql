-- V8__fix_created_at_default.sql
-- Garante default/NOT NULL para created_at e corrige registros antigos

ALTER TABLE patio ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
UPDATE patio SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
ALTER TABLE patio ALTER COLUMN created_at SET NOT NULL;
