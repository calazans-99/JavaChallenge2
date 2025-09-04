-- ======================
-- CRIAÇÃO DAS TABELAS
-- ======================

CREATE TABLE patio (
    id_patio BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    layout TEXT,
    CONSTRAINT uk_patio_nome_cidade UNIQUE (nome, cidade)
);

CREATE TABLE status_moto (
    id_status BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario VARCHAR(50) NOT NULL
);

CREATE TABLE moto (
    id_moto BIGSERIAL PRIMARY KEY,
    placa VARCHAR(8) NOT NULL UNIQUE,
    modelo VARCHAR(50) NOT NULL,
    id_patio BIGINT NOT NULL,
    id_status BIGINT NOT NULL,
    pos_x INT,
    pos_y INT,
    CONSTRAINT fk_moto_patio FOREIGN KEY (id_patio) REFERENCES patio(id_patio),
    CONSTRAINT fk_moto_status FOREIGN KEY (id_status) REFERENCES status_moto(id_status)
);

CREATE TABLE sensor (
    id_sensor BIGSERIAL PRIMARY KEY,
    id_moto BIGINT NOT NULL,
    temperatura NUMERIC,
    ligada CHAR(1) NOT NULL CHECK (ligada IN ('S','N')),
    CONSTRAINT fk_sensor_moto FOREIGN KEY (id_moto) REFERENCES moto(id_moto)
);

-- Índices extras para melhorar buscas
CREATE INDEX idx_patio_nome ON patio(nome);
CREATE INDEX idx_patio_cidade ON patio(cidade);
CREATE INDEX idx_status_descricao ON status_moto(descricao);
CREATE INDEX idx_usuario_nome ON usuario(nome);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_sensor_moto ON sensor(id_moto);
