BEGIN TRANSACTION;

DROP EXTENSION pgcrypto;

DROP TABLE tb_matched_player;

DROP TABLE tb_match;

DROP TABLE tb_server;

DROP TABLE tb_player;

END TRANSACTION;
