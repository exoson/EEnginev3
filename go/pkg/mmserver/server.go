package mmserver

import (
	"context"
	"fmt"
	"math"
	"strings"
	"sync"
	"time"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/exoson/EEnginev3/go/pkg/mmserver/persistence"
)

type queuedPlayer struct {
	Player   *api.Player
	LastBeat time.Time
}

type mmServer struct {
	db            persistence.Database
	queuedPlayers map[string]*queuedPlayer
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
		queuedPlayers: map[string]*queuedPlayer{},
		readyMatches:  map[string]*api.Match{},
	}, nil
}

func (mm *mmServer) CreateAccount(ctx context.Context, req *api.CreateAccountRequest) (*api.CreateAccountResponse, error) {
	if req.Player == nil {
		return nil, fmt.Errorf("Invalid account information")
	}
	err := mm.db.CreateAccount(req.Player)
	return &api.CreateAccountResponse{}, err
}

func (mm *mmServer) UpdateAccount(ctx context.Context, req *api.UpdateAccountRequest) (*api.UpdateAccountResponse, error) {
	if req.Player == nil {
		return nil, fmt.Errorf("Invalid account information")
	}
	ok, err := mm.db.AuthenticateAccount(req.Player)
	if err != nil {
		return nil, err
	}
	if !ok {
		return nil, fmt.Errorf("Wrong user or password")
	}
	req.Player.Password = req.NewPassword
	err = mm.db.UpdateAccount(req.Player)
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
	if player, ok := mm.queuedPlayers[req.Player.Name]; !ok {
		mm.queuedPlayers[req.Player.Name] = &queuedPlayer{
			Player:   req.Player,
			LastBeat: time.Now(),
		}
	} else {
		player.LastBeat = time.Now()
	}
	return &api.QueueResponse{}, nil
}

func (mm *mmServer) PollForMatch(ctx context.Context, req *api.PollForMatchRequest) (*api.PollForMatchResponse, error) {
	serverIp, err := mm.db.GetServerIp(req.ServerSecret)
	if err != nil {
		return nil, err
	}

	mm.queuedLock.Lock()

	timeouts := []*api.Player{}
	for _, player := range mm.queuedPlayers {
		if time.Since(player.LastBeat) > 2*time.Second {
			timeouts = append(timeouts, player.Player)
		}
	}
	for _, player := range timeouts {
		delete(mm.queuedPlayers, player.Name)
	}

	if len(mm.queuedPlayers) >= 2 {
		players := []*api.Player{}
		for _, player := range mm.queuedPlayers {
			players = append(players, player.Player)
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
	mm.readyLock.Lock()
	match := mm.readyMatches[req.Result.MatchId]
	delete(mm.readyMatches, req.Result.MatchId)
	mm.readyLock.Unlock()

	err := mm.db.UpdateMatchResult(req.Result)
	if err != nil {
		return nil, err
	}
	type perf struct {
		score          int64
		originalRating int64
	}
	if strings.HasPrefix(req.Result.Result, "winner") {
		perfs := []*perf{}
		for _, player := range match.Players {
			score := int64(0)
			if strings.HasSuffix(req.Result.Result, player.Name) {
				score = int64(1)
			}
			elo, err := mm.db.GetAccountElo(player)
			if err != nil {
				return nil, err
			}
			perfs = append(perfs, &perf{
				score:          score,
				originalRating: elo,
			})
		}
		nameA := match.Players[0].Name
		nameB := match.Players[1].Name
		qa := math.Pow(float64(10), float64(perfs[0].originalRating)/400)
		qb := math.Pow(float64(10), float64(perfs[1].originalRating)/400)
		ea := qa / (qa + qb)
		eb := qb / (qa + qb)
		k := float64(32)
		newA := perfs[0].originalRating + int64(k*(float64(perfs[0].score)-ea))
		newB := perfs[1].originalRating + int64(k*(float64(perfs[1].score)-eb))
		err := mm.db.UpdateAccountElo(&api.Player{Name: nameA, Elo: newA})
		if err != nil {
			return nil, err
		}
		err = mm.db.UpdateAccountElo(&api.Player{Name: nameB, Elo: newB})
		if err != nil {
			return nil, err
		}
	}
	return &api.MatchResultResponse{}, nil
}

func (mm *mmServer) ListPlayers(ctx context.Context, req *api.ListPlayersRequest) (*api.ListPlayersResponse, error) {
	players, err := mm.db.ListPlayers()
	return &api.ListPlayersResponse{Players: players}, err
}
