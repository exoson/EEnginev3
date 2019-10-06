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
	_, err := d.db.Exec("UPDATE tb_player SET password_hash = crypt(?, gen_salt('bf', 8) WHERE (name = ?);", req.Password, req.Name)
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

func (d *database) Close() error {
	return d.db.Close()
}
