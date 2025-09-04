-- V3__constraints_indexes.sql
-- Índices adicionais e view de relatório

create index if not exists idx_patio_nome on patio(nome);
create index if not exists idx_sensor_tipo on sensor(tipo);

create or replace view vw_qtd_motos_por_patio as
select p.id as patio_id, p.nome as patio_nome, count(m.id) as total_motos
  from patio p
  left join moto m on m.patio_id = p.id
 group by p.id, p.nome;