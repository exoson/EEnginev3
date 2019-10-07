[![Build Status](http://exxxooo.servegame.com:8080/buildStatus/icon?job=GithubHook)](http://exxxooo.servegame.com:8080/job/GithubHook/)

# EEnginev3
This is the third iteration of game engine 'EEngine'
The core is based on having server which runs the game logic and clients which connect to server and display the game and get input from user.

# MMserver
Match making server for EEnginev3

## Commands for MMserver
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=<passwd> -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
docker run --rm -v $PWD/go/pkg/mmserver/migrations:/migrations --network=host migrate/migrate -path=/migrations/ -database postgres://postgres:$PGPASSWORD@localhost:5432/db_mmserver?sslmode=disable up 1
