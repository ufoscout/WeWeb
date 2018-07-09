package service

import (
	"testing"
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/auth/model"
	"github.com/stretchr/testify/assert"
)

func Test_ShouldBeAuthenticated(t *testing.T) {
	user := model.Auth{0, "name", []string{}, make(map[string]interface{})}
	authContext := NewAuthContext(user)

	assert.NotNil(t, authContext)
	assert.True(t, authContext.isAuthenticated())
}


func Test_ShouldNotBeAuthenticated(t *testing.T) {
	user := model.Auth{0, "", []string{}, make(map[string]interface{})}
	authContext := NewAuthContext(user)

	assert.NotNil(t, authContext)
	assert.False(t, authContext.isAuthenticated())
}

func Test_ShouldNotBeAuthenticatedEvenIfHasRoles(t *testing.T) {
	user := model.Auth{0, "", []string{"role"}, make(map[string]interface{})}
	authContext := NewAuthContext(user)

	assert.NotNil(t, authContext)
	assert.False(t, authContext.isAuthenticated())
}

func Test_ShouldHAveRole(t *testing.T) {
	user := model.Auth{0, "", []string{"ADMIN"}, make(map[string]interface{})}
	authContext := NewAuthContext(user)

	assert.NotNil(t, authContext)
	assert.True(t, authContext.hasRole("ADMIN"))
}

func Test_ShouldHAveRole2(t *testing.T) {
	user := model.Auth{0, "", []string{"USER", "ADMIN"}, make(map[string]interface{})}
	authContext := NewAuthContext(user)

	assert.NotNil(t, authContext)
	assert.True(t, authContext.hasRole("ADMIN"))
}