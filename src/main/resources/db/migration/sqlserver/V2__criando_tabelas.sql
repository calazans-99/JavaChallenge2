-- V2__criando_tabelas.sql
-- Criação de tabelas base (idempotente) - SQL Server/Azure SQL
-- Todas as colunas de auditoria usam SYSUTCDATETIME() para consistência em cloud.

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

IF SCHEMA_ID('dbo') IS NULL EXEC('CREATE SCHEMA dbo');
GO

IF OBJECT_ID('dbo.funcao','U') IS NULL
BEGIN
  CREATE TABLE dbo.funcao (
    id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(40) NOT NULL UNIQUE
  );
END;
GO

IF OBJECT_ID('dbo.usuario','U') IS NULL
BEGIN
  CREATE TABLE dbo.usuario (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    username     VARCHAR(255) NOT NULL UNIQUE,
    senha        VARCHAR(255) NOT NULL,
    nome_perfil  VARCHAR(255) NULL,
    img_perfil   VARCHAR(255) NULL,
    created_at   DATETIME2 NOT NULL CONSTRAINT DF_usuario_created_at DEFAULT SYSUTCDATETIME()
  );
END;
GO

IF OBJECT_ID('dbo.usuario_funcao_tab','U') IS NULL
BEGIN
  CREATE TABLE dbo.usuario_funcao_tab (
    id_usuario BIGINT NOT NULL,
    id_funcao  BIGINT NOT NULL,
    CONSTRAINT pk_usuario_funcao PRIMARY KEY (id_usuario, id_funcao),
    CONSTRAINT fk_uf_usuario FOREIGN KEY (id_usuario) REFERENCES dbo.usuario(id),
    CONSTRAINT fk_uf_funcao  FOREIGN KEY (id_funcao)  REFERENCES dbo.funcao(id)
  );
END;
GO

IF OBJECT_ID('dbo.patio','U') IS NULL
BEGIN
  CREATE TABLE dbo.patio (
    id_patio     BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome         VARCHAR(60)  NOT NULL,
    localizacao  VARCHAR(120) NOT NULL,
    capacidade   INT          NOT NULL,
    -- dimensões serão adicionadas na V6
    created_at   DATETIME2    NOT NULL CONSTRAINT DF_patio_created_at DEFAULT SYSUTCDATETIME()
  );
END;
GO

IF OBJECT_ID('dbo.status_moto','U') IS NULL
BEGIN
  CREATE TABLE dbo.status_moto (
    id_status_moto BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome           VARCHAR(40) NOT NULL UNIQUE
  );
END;
GO

IF OBJECT_ID('dbo.sensor','U') IS NULL
BEGIN
  CREATE TABLE dbo.sensor (
    id_sensor BIGINT IDENTITY(1,1) PRIMARY KEY,
    tipo      VARCHAR(40) NOT NULL,
    status    VARCHAR(20) NOT NULL,
    pos_x     INT NULL,
    pos_y     INT NULL,
    id_patio  BIGINT NOT NULL,
    CONSTRAINT fk_sensor_patio FOREIGN KEY (id_patio) REFERENCES dbo.patio (id_patio)
  );
END;
GO

IF OBJECT_ID('dbo.moto','U') IS NULL
BEGIN
  CREATE TABLE dbo.moto (
    id_moto        BIGINT IDENTITY(1,1) PRIMARY KEY,
    modelo         VARCHAR(50) NOT NULL,
    placa          VARCHAR(8)  NOT NULL UNIQUE,
    pos_x          INT NULL,
    pos_y          INT NULL,
    id_patio       BIGINT NOT NULL,
    id_status_moto BIGINT NOT NULL,
    created_at     DATETIME2   NOT NULL CONSTRAINT DF_moto_created_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_moto_patio  FOREIGN KEY (id_patio)       REFERENCES dbo.patio (id_patio),
    CONSTRAINT fk_moto_status FOREIGN KEY (id_status_moto) REFERENCES dbo.status_moto (id_status_moto)
  );
END;
GO
