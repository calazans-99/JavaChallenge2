-- V2__seed_core.sql (compatível com H2 e PostgreSQL)
-- Seeds usando INSERT ... SELECT ... WHERE NOT EXISTS para evitar duplicatas.

-- STATUS_MOTO
INSERT INTO status_moto (nome)
SELECT 'ATIVA' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'ATIVA');
INSERT INTO status_moto (nome)
SELECT 'EM_MANUTENCAO' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'EM_MANUTENCAO');
INSERT INTO status_moto (nome)
SELECT 'INATIVA' WHERE NOT EXISTS (SELECT 1 FROM status_moto WHERE nome = 'INATIVA');

-- PÁTIO
INSERT INTO patio (nome, capacidade)
SELECT 'Pátio Central', 10 WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Central');
INSERT INTO patio (nome, capacidade)
SELECT 'Pátio Sul', 5 WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Sul');

-- SENSOR
INSERT INTO sensor (tipo, descricao)
SELECT 'GPS', 'Rastreamento de posição' WHERE NOT EXISTS (SELECT 1 FROM sensor WHERE tipo = 'GPS');
INSERT INTO sensor (tipo, descricao)
SELECT 'TEMP', 'Temperatura do motor' WHERE NOT EXISTS (SELECT 1 FROM sensor WHERE tipo = 'TEMP');