package mmserver

import (
	"context"
	"fmt"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	_ "google.golang.org/grpc"
)

type mmServer struct {
}

func NewMMServer() api.MatchMakingServer {
	return &mmServer{}
}

func (*mmServer) CreateAccount(ctx context.Context, req *api.CreateAccountRequest) (*api.CreateAccountResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (*mmServer) UpdateAccount(ctx context.Context, req *api.UpdateAccountRequest) (*api.UpdateAccountResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (*mmServer) Queue(ctx context.Context, req *api.QueueRequest) (*api.QueueResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (*mmServer) PollForMatch(ctx context.Context, req *api.PollForMatchRequest) (*api.PollForMatchResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (*mmServer) MatchResult(ctx context.Context, req *api.MatchResultRequest) (*api.MatchResultResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}
