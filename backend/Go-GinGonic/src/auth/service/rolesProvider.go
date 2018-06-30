package service

import "github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/auth/model"

type RoleProvider interface {
	getAll()                      []model.Role
	getByName(roleNames []string) []model.Role
}

type RoleProviderImpl struct {

}