-- V1__create_tables.sql
-- Esquema base (compatível com H2 2.x e PostgreSQL)

create table if not exists patio (
    id bigserial primary key,
    nome varchar(100) not null,
    capacidade int not null check (capacidade >= 0)
);

create table if not exists status_moto (
    id bigserial primary key,
    nome varchar(50) not null unique
);

create table if not exists sensor (
    id bigserial primary key,
    tipo varchar(50) not null,
    descricao varchar(255)
);

create table if not exists users (
    id bigserial primary key,
    username varchar(50) not null unique,
    password varchar(255) not null,
    role varchar(20) not null
);

create table if not exists moto (
    id bigserial primary key,
    modelo varchar(50) not null,
    placa varchar(8) not null unique,
    pos_x int,
    pos_y int,
    patio_id bigint references patio(id) on delete set null,
    status_id bigint references status_moto(id) on delete set null
);

create index if not exists idx_moto_patio on moto(patio_id);
create index if not exists idx_moto_status on moto(status_id);