package main

import (
	"bufio"
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"regexp"
	"strings"
	"time"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/golang/protobuf/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
)

var (
	crt = "server.crt"
)

func main() {
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("keystore password: ")
	ksPassword, err := reader.ReadString('\n')
	if err != nil {
		log.Fatal(err)
	}
	ksPassword = strings.Replace(ksPassword, "\n", "", -1)
	ksFlag := fmt.Sprintf("-Djavax.net.ssl.keyStorePassword=%s", ksPassword)

	mmServerAddress := "localhost:12321"
	if len(os.Args) > 1 {
		mmServerAddress = os.Args[1]
	}
	serverSecret := "servusala"

	creds, err := credentials.NewClientTLSFromFile(crt, "")
	if err != nil {
		log.Fatal(err)
	}
	_ = creds

	mmServerConnection, err := grpc.Dial(
		mmServerAddress,
		grpc.WithInsecure(),
		//grpc.WithTransportCredentials(creds),
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

			cmd := exec.Command("java", ksFlag, "-jar", "engine/src/main/java/server/game/server_deploy.jar", resp.Match.Server.MatchPassword)
			out, err := cmd.CombinedOutput()
			if err != nil {
				fmt.Println(string(out))
				log.Fatal(err)
			}

			var result string

			winnerRegexp := regexp.MustCompile("winner:[a-zA-Z0-9]*")
			winner := winnerRegexp.FindString(string(out))
			if len(winner) > 0 {
				fmt.Println(winner)
				result = winner
			} else {
				connectedRegexp := regexp.MustCompile("connected:[a-zA-Z0-9]*")
				connected := connectedRegexp.FindAllString(string(out), -1)
				result = strings.Join(connected, ";")
			}

			resultReq := &api.MatchResultRequest{
				ServerSecret: serverSecret,
				Result: &api.MatchResult{
					MatchId: resp.Match.Id,
					Result:  result,
				},
			}
			fmt.Println("Uploading results")
			_, err = mmServerClient.MatchResult(ctx, resultReq)
			if err != nil {
				log.Fatal(err)
			}
		} else {
			time.Sleep(1000 * time.Millisecond)
		}
	}
}
