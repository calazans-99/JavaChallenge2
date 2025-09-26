-- V5__seed_core.sql
-- Seeds núcleo: funções, status, admin e vínculo ADMIN->admin

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

-- Funções base
IF NOT EXISTS (SELECT 1 FROM dbo.funcao WHERE nome='ADMIN')
  INSERT INTO dbo.funcao (nome) VALUES ('ADMIN');
IF NOT EXISTS (SELECT 1 FROM dbo.funcao WHERE nome='COORDENADOR')
  INSERT INTO dbo.funcao (nome) VALUES ('COORDENADOR');
IF NOT EXISTS (SELECT 1 FROM dbo.funcao WHERE nome='PROFESSOR')
  INSERT INTO dbo.funcao (nome) VALUES ('PROFESSOR');
IF NOT EXISTS (SELECT 1 FROM dbo.funcao WHERE nome='DISCENTE')
  INSERT INTO dbo.funcao (nome) VALUES ('DISCENTE');

-- Status de moto
IF NOT EXISTS (SELECT 1 FROM dbo.status_moto WHERE nome='ATIVA')
  INSERT INTO dbo.status_moto (nome) VALUES ('ATIVA');
IF NOT EXISTS (SELECT 1 FROM dbo.status_moto WHERE nome='EM_MANUTENCAO')
  INSERT INTO dbo.status_moto (nome) VALUES ('EM_MANUTENCAO');
IF NOT EXISTS (SELECT 1 FROM dbo.status_moto WHERE nome='INATIVA')
  INSERT INTO dbo.status_moto (nome) VALUES ('INATIVA');

-- Usuário admin (hash BCrypt de "admin")
IF NOT EXISTS (SELECT 1 FROM dbo.usuario WHERE username='admin')
  INSERT INTO dbo.usuario (username, senha, img_perfil, nome_perfil)
  VALUES ('admin',
          '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO',
          'https://i0.wp.com/media.tumblr.com/tumblr_lga4hf2NWD1qfdzua.jpg',
          'Administrador');

-- Garante hash válido se algo estranho foi parar lá
UPDATE dbo.usuario
SET senha = '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO'
WHERE username = 'admin'
  AND (senha IS NULL OR senha NOT LIKE '$2%');

-- Vincula ADMIN ao admin
INSERT INTO dbo.usuario_funcao_tab (id_usuario, id_funcao)
SELECT u.id, f.id
FROM dbo.usuario u
JOIN dbo.funcao  f ON f.nome = 'ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM dbo.usuario_funcao_tab x
    WHERE x.id_usuario = u.id AND x.id_funcao = f.id
  );
