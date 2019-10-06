package main

import (
	"context"
	"testing"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver"
	"github.com/stretchr/testify/assert"
)

func TestMatchMaking(t *testing.T) {
	server, err := mmserver.NewMMServer()
	assert.Nil(t, err)

	ctx := context.Background()

	serverMsg := &api.Server{
		Ip:            "192.168.0.100",
		MatchPassword: "matsisala",
	}

	player1 := &api.Player{Name: "test1", Password: "test1"}
	player2 := &api.Player{Name: "test2", Password: "test2"}

	pollReq := &api.PollForMatchRequest{ServerSecret: "servusala"}
	pollResp, err := server.PollForMatch(ctx, pollReq)
	assert.Equal(t, pollResp, &api.PollForMatchResponse{})
	assert.Nil(t, err)

	/*createReq := &api.CreateAccountRequest{Player: player1}
	createResp, err := server.CreateAccount(ctx, createReq)
	assert.Equal(t, createResp, &api.CreateAccountResponse{})
	assert.Nil(t, err)
	createReq = &api.CreateAccountRequest{Player: player2}
	createResp, err = server.CreateAccount(ctx, createReq)
	assert.Equal(t, createResp, &api.CreateAccountResponse{})
	assert.Nil(t, err)*/

	queueReq := &api.QueueRequest{Player: player1}
	queueResp, err := server.Queue(ctx, queueReq)
	assert.Equal(t, queueResp, &api.QueueResponse{})
	assert.Nil(t, err)
	queueReq = &api.QueueRequest{Player: player2}
	queueResp, err = server.Queue(ctx, queueReq)
	assert.Equal(t, queueResp, &api.QueueResponse{})
	assert.Nil(t, err)

	pollReq = &api.PollForMatchRequest{ServerSecret: "servusala"}
	pollResp, err = server.PollForMatch(ctx, pollReq)
	assert.Nil(t, err)
	assert.NotNil(t, pollResp.Match)
	pollResp.Match.Id = ""
	assert.Equal(t, pollResp, &api.PollForMatchResponse{
		Match: &api.Match{
			Id:      "",
			Server:  serverMsg,
			Players: []*api.Player{player1, player2},
		},
	})

	queueReq = &api.QueueRequest{Player: player1}
	queueResp, err = server.Queue(ctx, queueReq)
	assert.Equal(t, queueResp, &api.QueueResponse{Server: serverMsg})
	assert.Nil(t, err)
	queueReq = &api.QueueRequest{Player: player2}
	queueResp, err = server.Queue(ctx, queueReq)
	assert.Equal(t, queueResp, &api.QueueResponse{Server: serverMsg})
	assert.Nil(t, err)

}
