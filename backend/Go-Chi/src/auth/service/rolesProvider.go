package service

import "github.com/ufoscout/WeWeb/backend/Go-Chi/src/auth/model"

type RoleProvider interface {
	getAll()                      []model.Role
	getByName(roleNames []string) []model.Role
}

type RoleProviderImpl struct {

}