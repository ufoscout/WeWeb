package json_test

import (
	"testing"
	"github.com/stretchr/testify/assert"
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/testUtil"
)

func Test_shouldMarshalToJson(t *testing.T) {

    group := colorGroup{
		ID:     1,
		Name:   "Reds",
		Colors: []string{"Crimson", "Red", "Ruby", "Maroon"},
	}

    json := testUtil.StaticAppContext().Services.Json
	b, err := json.ToJson(group)
	assert.Nil(t, err)
	assert.NotNil(t, b)

	assert.Equal(t, `{"ID":1,"Name":"Reds","Colors":["Crimson","Red","Ruby","Maroon"]}`, string(b))
}

func Test_shouldUnmarshalFromJson(t *testing.T) {

	var v colorGroup

	json := testUtil.StaticAppContext().Services.Json
	err := json.FromJson([]byte(`{"ID":1,"Name":"Grays","Colors":["Black","White"]}`), &v)
	assert.Nil(t, err)

	assert.Equal(t, 1, v.ID)
	assert.Equal(t, "Grays", v.Name)
	assert.Equal(t, "Black", v.Colors[0])
	assert.Equal(t, "White", v.Colors[1])
}

type colorGroup struct {
	ID     int
	Name   string
	Colors []string
}
