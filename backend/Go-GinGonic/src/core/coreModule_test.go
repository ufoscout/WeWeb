package core

import (
	"path"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/configuration"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/util"
)

func Test(t *testing.T) {

	config := configuration.Load(path.Join(util.MainFolderPath(), configuration.CONFIG_FILE_NAME))
	config.Server.Port = ":0"

	coreModule := New(&config)
	coreModule.Start()
	go coreModule.StartServer()

	time.Sleep(300 * time.Millisecond)

	assert.True(t, coreModule.ServerPort() > 0)

	coreModule.Stop()

}
