-- V4__alter_and_sample_business.sql
-- Adiciona a coluna 'ano' (compatível com H2 2.x e PostgreSQL)

ALTER TABLE moto ADD COLUMN IF NOT EXISTS ano INT;