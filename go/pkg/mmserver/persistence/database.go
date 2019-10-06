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
	port     = 5432
	user     = "postgres"
	password = "salasala"
	dbname   = "db_mmserver"

	DEFAULT_ELO = 1400
)

type Database interface {
	CreateAccount(*api.Player) error
	UpdateAccount(*api.Player) error
	AuthenticateAccount(*api.Player) (bool, error)
	GetServerIp(string) (string, error)
	CreateMatch(*api.Match, string) (string, error)
	UpdateMatchResult(*api.MatchResult) error

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
	id := uuid.New()
	_, err := d.db.Exec("INSERT INTO tb_player (id, name, password, elo) VALUES ( $1 , $2 , crypt( $3 , gen_salt('bf', 8)), $4 );", id.String(), req.Name, req.Password, DEFAULT_ELO)
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
	_, err := d.db.Exec("UPDATE tb_player SET password_hash = crypt($1, gen_salt('bf', 8) WHERE (name = $2);", req.Password, req.Name)
	return err
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
		return "", nil
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

func (d *database) Close() error {
	return d.db.Close()
}