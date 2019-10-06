#!/bash/bin
#docker run --rm -v $PWD/migrations:/migrations --network=host migrate/migrate -path=/migrations/ -database postgres://<user>:<passwd>@localhost:5432/db_mmserver?sslmode=disable down 1
#docker run --rm -v $PWD/migrations:/migrations --network=host migrate/migrate -path=/migrations/ -database postgres://<user>:<passwd>@localhost:5432/db_mmserver?sslmode=disable up 1

bazel test //go/pkg/mmserver/tests:go_default_test
