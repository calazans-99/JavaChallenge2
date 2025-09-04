-- ======================
-- DADOS INICIAIS
-- ======================

-- Status das motos
INSERT INTO status_moto (descricao) VALUES ('Disponível');
INSERT INTO status_moto (descricao) VALUES ('Em manutenção');
INSERT INTO status_moto (descricao) VALUES ('Roubada');

-- Pátios
INSERT INTO patio (nome, cidade, layout) VALUES ('Pátio Central', 'São Paulo', 'Layout exemplo 1');
INSERT INTO patio (nome, cidade, layout) VALUES ('Pátio Norte', 'Campinas', 'Layout exemplo 2');

-- Usuários
INSERT INTO usuario (nome, email, tipo_usuario) VALUES ('Lucas Abud Berbel', 'lucas@gmail.com', 'Administrador');
INSERT INTO usuario (nome, email, tipo_usuario) VALUES ('Marcus Calazans', 'marcus@gmail.com', 'Administrador');
INSERT INTO usuario (nome, email, tipo_usuario) VALUES ('Giovanni Lima', 'giovanni@gmail.com', 'Operador');

-- Motos
INSERT INTO moto (placa, modelo, id_patio, id_status, pos_x, pos_y)
VALUES ('ABC1D23', 'Honda CG 160', 1, 1, 10, 20);

INSERT INTO moto (placa, modelo, id_patio, id_status, pos_x, pos_y)
VALUES ('DEF4G56', 'Yamaha Fazer 250', 1, 2, 12, 18);

INSERT INTO moto (placa, modelo, id_patio, id_status, pos_x, pos_y)
VALUES ('GHI7J89', 'Suzuki GSR 150', 2, 1, 8, 22);

-- Sensores
INSERT INTO sensor (id_moto, temperatura, ligada) VALUES (1, 35.7, 'S');
INSERT INTO sensor (id_moto, temperatura, ligada) VALUES (2, 30.2, 'N');
