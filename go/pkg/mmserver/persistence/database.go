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
	CreateAccount(*api.CreateAccountRequest) error
	UpdateAccount(*api.UpdateAccountRequest) error

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

func (d *database) CreateAccount(req *api.CreateAccountRequest) error {
	tx, err := d.db.Begin()
	if err != nil {
		return err
	}
	id := uuid.New()
	_, err = tx.Exec("INSERT INTO tb_player (id, name, password, elo) VALUES ( $1 , $2 , crypt( $3 , gen_salt('bf', 8)), $4 );", id.String(), req.Player.Name, req.Player.Password, DEFAULT_ELO)
	if err != nil {
		tx.Rollback()
		return err
	}
	return tx.Commit()
}

func (d *database) UpdateAccount(req *api.UpdateAccountRequest) error {
	tx, err := d.db.Begin()
	if err != nil {
		return err
	}
	_, err = tx.Exec("UPDATE tb_player SET password_hash = crypt(?, gen_salt('bf', 8) WHERE (name = ?)", req.Player.Password, req.Player.Name)
	if err != nil {
		tx.Rollback()
		return err
	}
	return tx.Commit()

}

func (d *database) Close() error {
	return d.db.Close()
}
