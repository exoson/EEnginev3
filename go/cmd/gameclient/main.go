package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"time"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/golang/protobuf/proto"
	"google.golang.org/grpc"
)

func main() {
	mmServerAddress := "192.168.0.100:8080"
	mmServerConnection, err := grpc.Dial(
		mmServerAddress,
		grpc.WithInsecure(),
	)
	if err != nil {
		log.Fatal(err)
	}
	mmServerClient := api.NewMatchMakingClient(mmServerConnection)

	player := &api.Player{
		Name:     os.Args[1],
		Password: os.Args[2],
	}
	ctx := context.Background()
	mmServerClient.CreateAccount(ctx, &api.CreateAccountRequest{Player: player})
	for {
		req := &api.QueueRequest{
			Player: player,
		}
		resp, err := mmServerClient.Queue(ctx, req)
		if err != nil {
			log.Fatal(err)
		}
		emptyResp := &api.QueueResponse{}
		if !proto.Equal(resp, emptyResp) {
			secret := fmt.Sprintf("%s:%s", player.Name, resp.Server.MatchPassword)
			cmd := exec.Command("java", "-jar", "engine/src/main/java/client/game/client_deploy.jar", resp.Server.Ip, secret)
			err = cmd.Run()
			if err != nil {
				log.Fatal(err)
			}
			break
		} else {
			time.Sleep(1000 * time.Millisecond)
		}
	}
}
