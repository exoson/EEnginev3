package main

import (
	"log"
	"os"
	"net"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver"
	"google.golang.org/grpc"
)

func main() {
	MMServer, err := mmserver.NewMMServer()
	if err != nil {
		log.Fatal(err)
	}

	s := grpc.NewServer()
	api.RegisterMatchMakingServer(s, MMServer)

	port := ":12321"
	if len(os.Args) > 1 {
		port = os.Args[1]
	}
	sock, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatal(err)
	}
	if err := s.Serve(sock); err != nil {
		log.Fatal(err)
	}
}
