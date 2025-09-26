-- V0__extensions.sql
-- Padrão: SQL Server / Azure SQL
-- Objetivo: sane defaults e criação do schema dbo se necessário

SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

IF SCHEMA_ID('dbo') IS NULL
BEGIN
  EXEC('CREATE SCHEMA dbo');
END;
GO
