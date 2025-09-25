-- V4__ajustes_indices_e_seeds.sql (MySQL 8)
-- Índices equivalentes
CREATE INDEX idx_sensor_patio ON sensor (id_patio);
CREATE INDEX idx_moto_patio   ON moto   (id_patio);
CREATE INDEX idx_moto_status  ON moto   (id_status_moto);

-- (opcional – boa prática no MySQL: indexar FKs da tabela de junção)
-- CREATE INDEX idx_uf_id_usuario ON usuario_funcao_tab (id_usuario);
-- CREATE INDEX idx_uf_id_funcao  ON usuario_funcao_tab (id_funcao);

-- Seeds equivalentes (idempotentes)
INSERT INTO funcao (nome)
SELECT 'OPERADOR'
WHERE NOT EXISTS (SELECT 1 FROM funcao WHERE nome = 'OPERADOR');

INSERT INTO usuario (username, senha, nome_perfil, img_perfil)
SELECT 'oper',
       '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO',
       'Operador',
       'https://via.placeholder.com/100'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'oper');

INSERT INTO usuario_funcao_tab (id_usuario, id_funcao)
SELECT u.id, f.id
FROM usuario u
JOIN funcao  f ON f.nome = 'OPERADOR'
WHERE u.username = 'oper'
  AND NOT EXISTS (
    SELECT 1
    FROM usuario_funcao_tab x
    WHERE x.id_usuario = u.id AND x.id_funcao = f.id
  );
