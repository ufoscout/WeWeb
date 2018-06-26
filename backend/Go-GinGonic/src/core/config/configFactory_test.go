package config_test

import (
	"testing"

	"path"

	"github.com/stretchr/testify/assert"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
)

func Test_load_Unit(t *testing.T) {
	config := config.Load(path.Join(testUtil.MainFolderPath(), config.CONFIG_FILE_NAME))
	assert.Equal(t, "8080", config.Server.Port)
	assert.NotNil(t, config.Frontend.ResourcesPath)
}
