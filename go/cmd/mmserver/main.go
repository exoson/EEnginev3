package main

import (
	"log"
	"net"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver"
	"google.golang.org/grpc"
)

func main() {
	MMServer := mmserver.NewMMServer()

	s := grpc.NewServer()
	api.RegisterMatchMakingServer(s, MMServer)

	sock, err := net.Listen("tcp", ":8080")
	if err != nil {
		log.Fatal(err)
	}
	if err := s.Serve(sock); err != nil {
		log.Fatal(err)
	}
}
