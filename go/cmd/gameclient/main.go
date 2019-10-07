package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"runtime"
	"time"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/golang/protobuf/proto"
	"google.golang.org/grpc"
)

func main() {
	mmServerAddress := "exxxooo.servegame.com:12321"
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
			dir, err := os.Getwd()
			if err != nil {
				log.Fatal(err)
			}
			origLd := os.Getenv("LD_LIBRARY_PATH")
			origPath := os.Getenv("PATH")
			os.Setenv("LD_LIBRARY_PATH", fmt.Sprintf("%s:%s/native", origLd, dir))
			if runtime.GOOS == "windows" {
				os.Setenv("PATH", fmt.Sprintf("%s;%s/native", origLd, dir))
			}
			secret := fmt.Sprintf("%s:%s", player.Name, resp.Server.MatchPassword)
			cmd := exec.Command("java", "-jar", "engine/src/main/java/client/game/client_deploy.jar", resp.Server.Ip, secret)
			out, err := cmd.CombinedOutput()
			os.Setenv("LD_LIBRARY_PATH", origLd)
			os.Setenv("PATH", origPath)
			if err != nil {
				fmt.Println(string(out))
				log.Fatal(err)
			}
			break
		} else {
			time.Sleep(1000 * time.Millisecond)
		}
	}
}
