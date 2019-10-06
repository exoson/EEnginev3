package mmserver

import (
	"context"
	"fmt"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver/persistence"
	_ "google.golang.org/grpc"
)

type mmServer struct {
	db persistence.Database
}

func NewMMServer() api.MatchMakingServer {
	db, err := persistence.NewDatabase()
	if err != nil {
		panic(err)
	}
	return &mmServer{db: db}
}

func (mm *mmServer) CreateAccount(ctx context.Context, req *api.CreateAccountRequest) (*api.CreateAccountResponse, error) {
	err := mm.db.CreateAccount(req.Player)
	return &api.CreateAccountResponse{}, err
}

func (mm *mmServer) UpdateAccount(ctx context.Context, req *api.UpdateAccountRequest) (*api.UpdateAccountResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (mm *mmServer) Queue(ctx context.Context, req *api.QueueRequest) (*api.QueueResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (mm *mmServer) PollForMatch(ctx context.Context, req *api.PollForMatchRequest) (*api.PollForMatchResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}

func (mm *mmServer) MatchResult(ctx context.Context, req *api.MatchResultRequest) (*api.MatchResultResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}
