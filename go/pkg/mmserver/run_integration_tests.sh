#!/bin/bash
set -ex

psql -U postgres -h localhost -c 'drop database db_mmserver;' ||:
psql -U postgres -h localhost -c 'create database db_mmserver;'

docker run --rm -v $PWD/migrations:/migrations --network=host migrate/migrate -path=/migrations/ -database postgres://postgres:$PGPASSWORD@localhost:5432/db_mmserver?sslmode=disable down 1 ||:
docker run --rm -v $PWD/migrations:/migrations --network=host migrate/migrate -path=/migrations/ -database postgres://postgres:$PGPASSWORD@localhost:5432/db_mmserver?sslmode=disable up 1

# TODO() Do this in go
psql -U postgres -h localhost -d db_mmserver -c "insert into tb_server (id, ip, secret) values ('f1638488-1de7-457d-91d2-64f06c8edd1f', '85.188.34.25', 'servusala');"

bazel test --nocache_test_results --test_output=streamed //go/pkg/mmserver/tests:go_default_test
