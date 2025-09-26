-- V6__alter_patio_add_dimensao.sql
-- Adiciona colunas largura/altura ao dbo.patio, define defaults, NOT NULL e CHECK.

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

-- 1) Adiciona colunas se não existirem
IF COL_LENGTH('dbo.patio','largura') IS NULL
  ALTER TABLE dbo.patio ADD largura INT NULL;
IF COL_LENGTH('dbo.patio','altura') IS NULL
  ALTER TABLE dbo.patio ADD altura INT NULL;

-- 2) Popular valores nulos previamente
IF COL_LENGTH('dbo.patio','largura') IS NOT NULL
  UPDATE dbo.patio SET largura = 15 WHERE largura IS NULL;
IF COL_LENGTH('dbo.patio','altura') IS NOT NULL
  UPDATE dbo.patio SET altura  = 15 WHERE altura  IS NULL;

-- 3) Tornar NOT NULL e criar DEFAULT quando necessário
IF COL_LENGTH('dbo.patio','largura') IS NOT NULL
BEGIN
  ALTER TABLE dbo.patio ALTER COLUMN largura INT NOT NULL;

  IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c
      ON dc.parent_object_id = c.object_id
     AND dc.parent_column_id = c.column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.patio')
      AND c.name = 'largura'
  )
    ALTER TABLE dbo.patio ADD CONSTRAINT DF_patio_largura DEFAULT (15) FOR largura;
END

IF COL_LENGTH('dbo.patio','altura') IS NOT NULL
BEGIN
  ALTER TABLE dbo.patio ALTER COLUMN altura INT NOT NULL;

  IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c
      ON dc.parent_object_id = c.object_id
     AND dc.parent_column_id = c.column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.patio')
      AND c.name = 'altura'
  )
    ALTER TABLE dbo.patio ADD CONSTRAINT DF_patio_altura DEFAULT (15) FOR altura;
END

-- 4) CHECK constraint
IF NOT EXISTS (
  SELECT 1
  FROM sys.check_constraints
  WHERE name = 'CK_patio_largura_altura_positive'
    AND parent_object_id = OBJECT_ID('dbo.patio')
)
  ALTER TABLE dbo.patio
    ADD CONSTRAINT CK_patio_largura_altura_positive
    CHECK (largura > 0 AND altura > 0);
