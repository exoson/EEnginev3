package persistence

import (
	"database/sql"
	"fmt"

	api "github.com/exoson/EEnginev3/api/proto/mmserver"
	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

const (
	host     = "localhost"
	port     = 5433
	user     = "postgres"
	password = "salasala"
	dbname   = "db_mmserver"

	DEFAULT_ELO = 1400
)

type Database interface {
	CreateAccount(*api.Player) error
	UpdateAccount(*api.Player) error
	UpdateAccountElo(*api.Player) error
	GetAccountElo(*api.Player) (int64, error)
	AuthenticateAccount(*api.Player) (bool, error)
	GetServerIp(string) (string, error)
	CreateMatch(*api.Match, string) (string, error)
	UpdateMatchResult(*api.MatchResult) error
	ListPlayers() ([]*api.Player, error)

	Close() error
}

type database struct {
	db *sql.DB
}

func NewDatabase() (Database, error) {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		return nil, err
	}
	return &database{db: db}, nil
}

func (d *database) CreateAccount(req *api.Player) error {
	fmt.Println("PERKELE")
	id := uuid.New()
	_, err := d.db.Exec("INSERT INTO tb_player (id, name, password, elo) VALUES ( $1 , $2 , crypt( $3 , gen_salt('bf', 8)), $4 );", id.String(), req.Name, req.Password, DEFAULT_ELO)
	
	fmt.Println(err)
	return err
}

func (d *database) AuthenticateAccount(req *api.Player) (bool, error) {
	rows, err := d.db.Query("SELECT * FROM tb_player WHERE (name = $1) AND (password = crypt( $2 , password));", req.Name, req.Password)
	if err != nil {
		return false, err
	}
	ok := rows.Next()
	rows.Close()
	return ok, nil
}

func (d *database) UpdateAccount(req *api.Player) error {
	_, err := d.db.Exec("UPDATE tb_player SET password = crypt($1, gen_salt('bf', 8)) WHERE (name = $2);", req.Password, req.Name)
	return err
}

func (d *database) UpdateAccountElo(req *api.Player) error {
	_, err := d.db.Exec("UPDATE tb_player SET elo = $1 WHERE (name = $2);", req.Elo, req.Name)
	return err
}

func (d *database) GetAccountElo(req *api.Player) (int64, error) {
	rows, err := d.db.Query("SELECT elo FROM tb_player WHERE (name = $1);", req.Name)
	if err != nil {
		return int64(-1), err
	}
	defer rows.Close()
	ok := rows.Next()
	var elo int64
	if ok {
		err = rows.Scan(&elo)
	} else {
		err = fmt.Errorf("Failed to find player from table")
	}
	if err != nil {
		return int64(-1), err
	}
	return elo, nil
}

func (d *database) GetServerIp(serverSecret string) (string, error) {
	rows, err := d.db.Query("SELECT ip FROM tb_server WHERE (secret = $1);", serverSecret)
	if err != nil {
		return "", err
	}
	ok := rows.Next()
	var ip string
	if ok {
		err = rows.Scan(&ip)
	} else {
		err = fmt.Errorf("Failed to find server from table")
	}
	rows.Close()
	if err != nil {
		return "", err
	}
	return ip, nil
}

func (d *database) CreateMatch(req *api.Match, serverSecret string) (string, error) {
	tx, err := d.db.Begin()
	if err != nil {
		return "", err
	}
	id := uuid.New().String()
	_, err = tx.Exec("INSERT INTO tb_match (id, server_id) SELECT $1, tb_server.id FROM tb_server WHERE tb_server.secret = $2;", id, serverSecret)
	if err != nil {
		tx.Rollback()
		return "", err
	}

	for _, player := range req.Players {
		_, err := tx.Exec("INSERT INTO tb_matched_player (match_id, player_id, team_id) SELECT $1, tb_player.id, 0 FROM tb_player WHERE tb_player.name = $2;", id, player.Name)
		if err != nil {
			tx.Rollback()
			return "", err
		}
	}
	err = tx.Commit()
	return id, err
}

func (d *database) UpdateMatchResult(req *api.MatchResult) error {
	_, err := d.db.Exec("UPDATE tb_match SET result = $1, end_time = CURRENT_TIMESTAMP WHERE (id = $2);", req.Result, req.MatchId)
	return err
}

func (d *database) ListPlayers() ([]*api.Player, error) {
	rows, err := d.db.Query("SELECT name, elo FROM tb_player ORDER BY elo DESC LIMIT 50;")
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	players := []*api.Player{}
	for rows.Next() {
		var name string
		var elo int64
		err = rows.Scan(&name, &elo)
		if err != nil {
			return nil, err
		}
		players = append(players, &api.Player{Name: name, Elo: elo})
	}
	return players, nil
}

func (d *database) Close() error {
	return d.db.Close()
}
