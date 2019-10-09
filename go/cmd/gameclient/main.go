package main

import (
	"bufio"
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"runtime"
	"time"
	"strings"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/golang/protobuf/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
)

var (
	crt = "server.crt"
)

func main() {
	mmServerAddress := "exxxooo.servegame.com:12321"
	if len(os.Args) > 3 {
		mmServerAddress = os.Args[3]
	}

	creds, err := credentials.NewClientTLSFromFile(crt, "")
	if err != nil {
		log.Fatal(err)
	}

	mmServerConnection, err := grpc.Dial(
		mmServerAddress,
		grpc.WithTransportCredentials(creds),
	)
	if err != nil {
		log.Fatal(err)
	}

	mmServerClient := api.NewMatchMakingClient(mmServerConnection)

	player := &api.Player{
		Name:     os.Args[1],
		Password: os.Args[2],
	}
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("truststore password: ")
	tsPassword, err := reader.ReadString('\n')
	if err != nil {
		log.Fatal(err)
	}
	tsPassword = strings.Replace(tsPassword, "\n", "", -1)
	tsFlag := fmt.Sprintf("-Djavax.net.ssl.trustStorePassword=%s", tsPassword)
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
			secret := fmt.Sprintf("%s:%s", player.Name, resp.Server.MatchPassword)
			flags := []string{}
			if runtime.GOOS == "darwin" {
				flags = append(flags, "-XstartOnFirstThread")
			}
			lwjglNativeFlag := fmt.Sprintf("-Djava.library.path=%s/native", dir)
			flags = append(flags, tsFlag, lwjglNativeFlag, "-jar", "engine/src/main/java/client/game/client_deploy.jar", resp.Server.Ip, secret)
			cmd := exec.Command("java", flags...)
			out, err := cmd.CombinedOutput()
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
