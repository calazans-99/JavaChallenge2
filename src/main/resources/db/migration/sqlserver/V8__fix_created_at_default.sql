-- V8__fix_created_at_default.sql
-- Padroniza defaults de created_at para SYSUTCDATETIME() na tabela dbo.patio

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

-- Garante valores não nulos antes do NOT NULL
UPDATE dbo.patio SET created_at = SYSUTCDATETIME() WHERE created_at IS NULL;

-- Descobre e remove default atual (se houver)
DECLARE @dcname sysname;
SELECT @dcname = dc.name
FROM sys.default_constraints dc
JOIN sys.columns c
  ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
WHERE dc.parent_object_id = OBJECT_ID('dbo.patio')
  AND c.name = 'created_at';

IF @dcname IS NOT NULL
BEGIN
  DECLARE @sql NVARCHAR(400) = N'ALTER TABLE dbo.patio DROP CONSTRAINT ' + QUOTENAME(@dcname) + N';';
  EXEC sp_executesql @sql;
END;

-- Recria default com SYSUTCDATETIME()
ALTER TABLE dbo.patio ADD CONSTRAINT DF_patio_created_at DEFAULT (SYSUTCDATETIME()) FOR created_at;

-- Garante NOT NULL
ALTER TABLE dbo.patio ALTER COLUMN created_at DATETIME2 NOT NULL;
