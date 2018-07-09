package service

import "github.com/ufoscout/WeWeb/backend/Go-Chi/src/auth/model"

type AuthContext interface {
	isAuthenticated() bool
	hasRole(roleName string) bool
	hasAnyRole(roleNames ...string) bool
	hasAllRoles(roleNames ...string) bool
}

func NewAuthContext(auth model.Auth) AuthContext {
	return &authContextImpl{
		Auth: auth,
	}
}

type authContextImpl struct {
	Auth model.Auth;
}

func (ac *authContextImpl) isAuthenticated() bool {
	return ac.Auth.Username != ""
}

func (ac *authContextImpl) hasRole(roleName string) bool {
	return contains(ac.Auth.Roles, roleName)
}

func (ac *authContextImpl) hasAnyRole(roleNames ...string) bool {
	return true
}

func (ac *authContextImpl) hasAllRoles(roleNames ...string) bool {
	return true
}


func contains(s []string, e string) bool {
	for _, a := range s {
		if a == e {
			return true
		}
	}
	return false
}
