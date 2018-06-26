package core_test

import (
	"path"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
)

func Test(t *testing.T) {

	config := config.Load(path.Join(testUtil.MainFolderPath(), config.CONFIG_FILE_NAME))
	config.Server.Port = "0"

	coreModule := core.New(&config)
	coreModule.Start()
	go coreModule.StartServer()

	time.Sleep(300 * time.Millisecond)

	assert.True(t, coreModule.ServerPort() > 0)

	coreModule.Stop()

}
