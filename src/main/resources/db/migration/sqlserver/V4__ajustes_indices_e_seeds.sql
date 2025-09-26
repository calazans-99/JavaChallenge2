-- V4__ajustes_indices_e_seeds.sql
-- Índices e seeds básicos (idempotente). Padrão: IF NOT EXISTS + INSERT.

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

-- Índices (verifica por nome/objeto)
IF NOT EXISTS (
  SELECT 1 FROM sys.indexes
  WHERE name = 'idx_sensor_patio' AND object_id = OBJECT_ID('dbo.sensor')
)
  CREATE INDEX idx_sensor_patio ON dbo.sensor (id_patio);
GO

IF NOT EXISTS (
  SELECT 1 FROM sys.indexes
  WHERE name = 'idx_moto_patio' AND object_id = OBJECT_ID('dbo.moto')
)
  CREATE INDEX idx_moto_patio ON dbo.moto (id_patio);
GO

IF NOT EXISTS (
  SELECT 1 FROM sys.indexes
  WHERE name = 'idx_moto_status' AND object_id = OBJECT_ID('dbo.moto')
)
  CREATE INDEX idx_moto_status ON dbo.moto (id_status_moto);
GO

-- Seeds equivalentes (idempotentes)
IF NOT EXISTS (SELECT 1 FROM dbo.funcao WHERE nome = 'OPERADOR')
  INSERT INTO dbo.funcao (nome) VALUES ('OPERADOR');

IF NOT EXISTS (SELECT 1 FROM dbo.usuario WHERE username = 'oper')
  INSERT INTO dbo.usuario (username, senha, nome_perfil, img_perfil)
  VALUES ('oper',
          '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO', -- bcrypt('admin') de exemplo
          'Operador',
          'https://via.placeholder.com/100');

-- Vincula OPERADOR -> oper (sem duplicar)
INSERT INTO dbo.usuario_funcao_tab (id_usuario, id_funcao)
SELECT u.id, f.id
FROM dbo.usuario u
JOIN dbo.funcao  f ON f.nome = 'OPERADOR'
WHERE u.username = 'oper'
  AND NOT EXISTS (
    SELECT 1 FROM dbo.usuario_funcao_tab x
    WHERE x.id_usuario = u.id AND x.id_funcao = f.id
  );
