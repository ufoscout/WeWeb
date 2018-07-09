package module

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func Test_ShouldInitAllModulesThenStartThem(t *testing.T) {

	mod1 := mod{name: "one"}
	mod2 := mod{name: "two"}

	Go(&mod1, &mod2)

	assert.Equal(t, "one-init", actions[0])
	assert.Equal(t, "two-init", actions[1])
	assert.Equal(t, "one-start", actions[2])
	assert.Equal(t, "two-start", actions[3])
}

var actions []string

type mod struct {
	name string
}

func (m *mod) Init() error {
	actions = append(actions, m.name + "-init")
	return nil
}

func (m *mod) Start() error {
	actions = append(actions, m.name + "-start")
	return nil
}
