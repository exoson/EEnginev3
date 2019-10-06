package mmserver

import (
	"context"
	"fmt"
	"sync"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver/persistence"
)

type mmServer struct {
	db            persistence.Database
	queuedPlayers map[string]*api.Player
	readyMatches  map[string]*api.Match
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
		readyMatches:  map[string]*api.Match{},
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
	req.Player.Password = ""

	mm.readyLock.Lock()
	for _, match := range mm.readyMatches {
		for _, player := range match.Players {
			if player.Name == req.Player.Name {
				mm.readyLock.Unlock()
				return &api.QueueResponse{
					Server: match.Server,
				}, nil
			}
		}
	}
	mm.readyLock.Unlock()

	mm.queuedLock.Lock()
	defer mm.queuedLock.Unlock()
	if _, ok := mm.queuedPlayers[req.Player.Name]; !ok {
		mm.queuedPlayers[req.Player.Name] = req.Player
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
		match := &api.Match{
			Server: &api.Server{
				Ip:            serverIp,
				MatchPassword: "matsisala",
			},
			Players: players,
		}
		id, err := mm.db.CreateMatch(match, req.ServerSecret)
		if err != nil {
			return nil, err
		}
		match.Id = id

		mm.readyLock.Lock()
		mm.readyMatches[match.Id] = match
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
	err := mm.db.UpdateMatchResult(req.Result)
	mm.readyLock.Lock()
	delete(mm.readyMatches, req.Result.MatchId)
	mm.readyLock.Unlock()
	if err != nil {
		return nil, err
	} else {
		return &api.MatchResultResponse{}, nil
	}
}
