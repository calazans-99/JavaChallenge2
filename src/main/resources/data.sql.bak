-- ======= PÁTIO =======
INSERT INTO patio (nome, localizacao, capacidade)
SELECT 'Pátio Principal', 'Bloco A', 100
WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Principal');

-- ======= STATUS =======
INSERT INTO status_moto (nome)
SELECT 'ATIVA' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'ATIVA');

INSERT INTO status_moto (nome)
SELECT 'EM_MANUTENCAO' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'EM_MANUTENCAO');

INSERT INTO status_moto (nome)
SELECT 'INATIVA' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'INATIVA');

-- ======= SENSOR =======
INSERT INTO sensor (tipo, status, pos_x, pos_y, id_patio)
SELECT 'PRESENCA', 'ATIVO', 0, 0, p.id_patio
FROM patio p
WHERE p.nome = 'Pátio Principal'
  AND NOT EXISTS (SELECT 1 FROM sensor s WHERE s.tipo='PRESENCA' AND s.id_patio = p.id_patio);

-- ======= MOTO =======
INSERT INTO moto (modelo, placa, pos_x, pos_y, id_patio, id_status_moto)
SELECT 'PCX 160', 'ABC-1234', 1, 1, p.id_patio, sm.id_status_moto
FROM patio p
JOIN status_moto sm ON sm.nome = 'ATIVA'
WHERE p.nome = 'Pátio Principal'
  AND NOT EXISTS (SELECT 1 FROM moto m WHERE m.placa = 'ABC-1234');

-- ======= USUÁRIO ADMIN =======
-- username: admin | senha: admin (bcrypt fixo abaixo)
INSERT INTO usuario (username, senha, img_perfil, nome_perfil)
SELECT 'admin',
       '$2a$12$h227p1QzQEB2cIW/BrzZletfr20O0lNDBMYZM0K6z5faY6bJ17kpO',
       'https://i0.wp.com/media.tumblr.com/tumblr_lga4hf2NWD1qfdzua.jpg',
       'Administrador'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'admin');

-- Vincula ADMIN ao usuário admin, sem duplicar
INSERT INTO usuario_funcao_tab (id_usuario, id_funcao)
SELECT u.id, f.id
FROM usuario u
JOIN funcao f ON f.nome = 'ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
      SELECT 1 FROM usuario_funcao_tab x
      WHERE x.id_usuario = u.id AND x.id_funcao = f.id
  );
