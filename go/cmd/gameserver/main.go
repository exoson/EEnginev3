package main

import (
	"context"
	"fmt"
	"log"
	"os/exec"
	"time"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/golang/protobuf/proto"
	"google.golang.org/grpc"
)

func main() {
	mmServerAddress := "192.168.0.100:8080"
	serverSecret := "servusala"
	mmServerConnection, err := grpc.Dial(
		mmServerAddress,
		grpc.WithInsecure(),
	)
	if err != nil {
		log.Fatal(err)
	}
	mmServerClient := api.NewMatchMakingClient(mmServerConnection)

	for {
		req := &api.PollForMatchRequest{
			ServerSecret: serverSecret,
		}
		ctx := context.Background()
		resp, err := mmServerClient.PollForMatch(ctx, req)
		if err != nil {
			log.Fatal(err)
		}
		emptyResp := &api.PollForMatchResponse{}
		if !proto.Equal(resp, emptyResp) {
			fmt.Println("Recieved match")
			fmt.Println(resp)

			// RUN GAME
			cmd := exec.Command("engine/src/main/java/server/game/server", resp.Match.Server.MatchPassword)
			err = cmd.Run()
			if err != nil {
				log.Fatal(err)
			}

			resultReq := &api.MatchResultRequest{
				ServerSecret: serverSecret,
				Result: &api.MatchResult{
					MatchId: resp.Match.Id,
					Result:  "0",
				},
			}
			fmt.Println("Uploading results")
			_, err := mmServerClient.MatchResult(ctx, resultReq)
			if err != nil {
				log.Fatal(err)
			}
		} else {
			time.Sleep(1000 * time.Millisecond)
		}
	}
}
