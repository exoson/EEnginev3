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
	tx, err := d.db.Begin()
	if err != nil {
		return err
	}
	id := uuid.New()
	_, err = tx.Exec("INSERT INTO tb_player (id, name, password, elo) VALUES ( $1 , $2 , crypt( $3 , gen_salt('bf', 8)), $4 );", id.String(), req.Name, req.Password, DEFAULT_ELO)
	if err != nil {
		tx.Rollback()
		return err
	}
	return tx.Commit()
}

func (d *database) AuthenticateAccount(req *api.Player) (bool, error) {
	tx, err := d.db.Begin()
	if err != nil {
		return false, err
	}
	rows, err := tx.Query("SELECT * FROM tb_player WHERE (name = $1) AND (password = crypt( $2 , password));", req.Name, req.Password)
	if err != nil {
		tx.Rollback()
		return false, err
	}
	ok := rows.Next()
	rows.Close()
	err = tx.Commit()
	if err != nil {
		return false, err
	}
	return ok, nil
}

func (d *database) UpdateAccount(req *api.Player) error {
	tx, err := d.db.Begin()
	if err != nil {
		return err
	}
	_, err = tx.Exec("UPDATE tb_player SET password_hash = crypt(?, gen_salt('bf', 8) WHERE (name = ?);", req.Password, req.Name)
	if err != nil {
		tx.Rollback()
		return err
	}
	return tx.Commit()

}

func (d *database) Close() error {
	return d.db.Close()
}
