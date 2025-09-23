-- V7__seed_patios_dimensoes.sql
-- Popula pátios adicionais com dimensões variadas para testes no app mobile

-- Pátio Secundário (10x10)
INSERT INTO patio (nome, localizacao, capacidade, largura, altura, created_at)
SELECT 'Pátio Secundário', 'Unidade Zona Norte', 50, 10, 10, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Secundário');

-- Pátio Experimental (12x15)
INSERT INTO patio (nome, localizacao, capacidade, largura, altura, created_at)
SELECT 'Pátio Experimental', 'Unidade Zona Sul', 80, 12, 15, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Experimental');

-- Pátio Ampliado (20x12)
INSERT INTO patio (nome, localizacao, capacidade, largura, altura, created_at)
SELECT 'Pátio Ampliado', 'Unidade Central', 120, 20, 12, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM patio WHERE nome = 'Pátio Ampliado');
