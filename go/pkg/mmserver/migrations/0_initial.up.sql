BEGIN TRANSACTION;

CREATE EXTENSION pgcrypto;

CREATE TABLE tb_player (
    id uuid NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    elo integer NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE tb_server (
    id uuid NOT NULL,
    ip VARCHAR(255) NOT NULL,
    secret VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (secret)
);

CREATE TABLE tb_match (
    id uuid NOT NULL,
    server_id uuid REFERENCES tb_server(id),
    result VARCHAR(255),
    end_time timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE tb_matched_player (
    match_id uuid REFERENCES tb_match(id),
    player_id uuid REFERENCES tb_player(id),
    team_id integer NOT NULL,
    PRIMARY KEY (match_id, player_id)
);

END TRANSACTION;
