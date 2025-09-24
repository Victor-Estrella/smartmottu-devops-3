-- V4 - Inserts iniciais (SQL Server)
-- Usuários
insert into T_SMARTMOTTU_USUARIO (nome, email, senha) values ('Mauricio Silva', 'mauricio@gmail.com', 'senha123');
insert into T_SMARTMOTTU_USUARIO (nome, email, senha) values ('Rodrigo Vieira', 'rodrigo@gmail.com', 'senha456');

-- Tipos de Moto
insert into T_SMARTMOTTU_TIPO_MOTO (nm_tipo) values ('Pop');
insert into T_SMARTMOTTU_TIPO_MOTO (nm_tipo) values ('Sport');
insert into T_SMARTMOTTU_TIPO_MOTO (nm_tipo) values ('Elétrica');

-- Status de Moto
insert into T_SMARTMOTTU_STATUS_MOTO (status, data) values ('Disponível', '2025-09-24');
insert into T_SMARTMOTTU_STATUS_MOTO (status, data) values ('Em manutenção', '2025-09-20');
insert into T_SMARTMOTTU_STATUS_MOTO (status, data) values ('Alugada', '2025-09-10');

-- Motos (FKs: tipo e status)
insert into T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) values ('9BWZZZ377VT004251', 'ABC1234', 'Butantã', 1, 2);
insert into T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) values ('9BWZZZ377VT004252', 'DEF5678', 'Carrão', 2, 3);
insert into T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) values ('9BWZZZ377VT004253', 'GHI9012', 'Brás', 3, 1);