package mmserver

import (
	"context"
	"fmt"
	"sync"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver/persistence"
	"github.com/google/uuid"
	_ "google.golang.org/grpc"
)

type mmServer struct {
	db            persistence.Database
	queuedPlayers map[string]*api.Player
	readyMatches  []*api.Match
	queuedLock    sync.Mutex
	readyLock     sync.Mutex
}

func NewMMServer() (api.MatchMakingServer, error) {
	db, err := persistence.NewDatabase()
	if err != nil {
		return nil, err
	}
	return &mmServer{
		db:            db,
		queuedPlayers: map[string]*api.Player{},
		readyMatches:  []*api.Match{},
	}, nil
}

func (mm *mmServer) CreateAccount(ctx context.Context, req *api.CreateAccountRequest) (*api.CreateAccountResponse, error) {
	err := mm.db.CreateAccount(req.Player)
	return &api.CreateAccountResponse{}, err
}

func (mm *mmServer) UpdateAccount(ctx context.Context, req *api.UpdateAccountRequest) (*api.UpdateAccountResponse, error) {
	err := mm.db.UpdateAccount(req.Player)
	return &api.UpdateAccountResponse{}, err
}

func (mm *mmServer) Queue(ctx context.Context, req *api.QueueRequest) (*api.QueueResponse, error) {
	ok, err := mm.db.AuthenticateAccount(req.Player)
	if err != nil {
		return nil, err
	}
	if !ok {
		return nil, fmt.Errorf("Wrong user or password")
	}

	mm.queuedLock.Lock()
	if _, ok := mm.queuedPlayers[req.Player.Name]; !ok {
		mm.queuedPlayers[req.Player.Name] = req.Player
	}
	mm.queuedLock.Unlock()
	mm.readyLock.Lock()
	defer mm.readyLock.Unlock()
	for _, match := range mm.readyMatches {
		for _, player := range match.Players {
			if player.Name == req.Player.Name {
				return &api.QueueResponse{
					Server: match.Server,
				}, nil
			}
		}
	}
	return &api.QueueResponse{}, nil
}

func (mm *mmServer) PollForMatch(ctx context.Context, req *api.PollForMatchRequest) (*api.PollForMatchResponse, error) {
	serverIp, err := mm.db.GetServerIp(req.ServerSecret)
	if err != nil {
		return nil, err
	}
	mm.queuedLock.Lock()
	if len(mm.queuedPlayers) >= 2 {
		players := []*api.Player{}
		for _, player := range mm.queuedPlayers {
			players = append(players, player)
			if len(players) == 2 {
				break
			}
		}
		for _, player := range players {
			delete(mm.queuedPlayers, player.Name)
		}
		mm.queuedLock.Unlock()
		id := uuid.New()
		match := &api.Match{
			Id: id.String(),
			Server: &api.Server{
				Ip:            serverIp,
				MatchPassword: "matsisala",
			},
			Players: players,
		}
		mm.readyLock.Lock()
		mm.readyMatches = append(mm.readyMatches, match)
		mm.readyLock.Unlock()
		return &api.PollForMatchResponse{
			Match: match,
		}, nil
	} else {
		mm.queuedLock.Unlock()
	}
	return &api.PollForMatchResponse{}, nil
}

func (mm *mmServer) MatchResult(ctx context.Context, req *api.MatchResultRequest) (*api.MatchResultResponse, error) {
	return nil, fmt.Errorf("Not implemented")
}
