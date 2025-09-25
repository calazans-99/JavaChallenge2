-- V2__criando_tabelas.sql (MySQL 8)

CREATE TABLE IF NOT EXISTS funcao (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(40) NOT NULL,
  CONSTRAINT pk_funcao PRIMARY KEY (id),
  CONSTRAINT uk_funcao_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS usuario (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  username     VARCHAR(255) NOT NULL,
  senha        VARCHAR(255) NOT NULL,
  nome_perfil  VARCHAR(255) NULL,
  img_perfil   VARCHAR(255) NULL,
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_usuario PRIMARY KEY (id),
  CONSTRAINT uk_usuario_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS usuario_funcao_tab (
  id_usuario BIGINT NOT NULL,
  id_funcao  BIGINT NOT NULL,
  CONSTRAINT pk_usuario_funcao PRIMARY KEY (id_usuario, id_funcao),
  CONSTRAINT fk_uf_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id),
  CONSTRAINT fk_uf_funcao  FOREIGN KEY (id_funcao)  REFERENCES funcao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS patio (
  id_patio     BIGINT NOT NULL AUTO_INCREMENT,
  nome         VARCHAR(60)  NOT NULL,
  localizacao  VARCHAR(120) NOT NULL,
  capacidade   INT          NOT NULL,
  created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_patio PRIMARY KEY (id_patio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS status_moto (
  id_status_moto BIGINT NOT NULL AUTO_INCREMENT,
  nome           VARCHAR(40) NOT NULL,
  CONSTRAINT pk_status_moto PRIMARY KEY (id_status_moto),
  CONSTRAINT uk_status_moto_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sensor (
  id_sensor BIGINT NOT NULL AUTO_INCREMENT,
  tipo      VARCHAR(40) NOT NULL,
  `status`  VARCHAR(20) NOT NULL,
  pos_x     INT NULL,
  pos_y     INT NULL,
  id_patio  BIGINT NOT NULL,
  CONSTRAINT pk_sensor PRIMARY KEY (id_sensor),
  CONSTRAINT fk_sensor_patio FOREIGN KEY (id_patio) REFERENCES patio (id_patio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS moto (
  id_moto        BIGINT NOT NULL AUTO_INCREMENT,
  modelo         VARCHAR(50) NOT NULL,
  placa          VARCHAR(8)  NOT NULL,
  pos_x          INT NULL,
  pos_y          INT NULL,
  id_patio       BIGINT NOT NULL,
  id_status_moto BIGINT NOT NULL,
  created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_moto PRIMARY KEY (id_moto),
  CONSTRAINT uk_moto_placa UNIQUE (placa),
  CONSTRAINT fk_moto_patio  FOREIGN KEY (id_patio)       REFERENCES patio (id_patio),
  CONSTRAINT fk_moto_status FOREIGN KEY (id_status_moto) REFERENCES status_moto (id_status_moto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
