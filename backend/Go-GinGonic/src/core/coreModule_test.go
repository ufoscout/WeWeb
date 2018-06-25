package core

import (
	"path"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/util"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
)

func Test(t *testing.T) {

	config := config.Load(path.Join(util.MainFolderPath(), config.CONFIG_FILE_NAME))
	config.Server.Port = "0"

	coreModule := New(&config)
	coreModule.Start()
	go coreModule.StartServer()

	time.Sleep(300 * time.Millisecond)

	assert.True(t, coreModule.ServerPort() > 0)

	coreModule.Stop()

}
