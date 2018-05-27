package core

import (
	"fmt"

	"github.com/gin-contrib/static"
	"github.com/gin-gonic/gin"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/configuration"

	"context"
	"log"
	"net"
	"net/http"
	"time"
)

type CoreModule struct {
	config     *configuration.Config
	ginRouter  *gin.Engine
	httpServer *http.Server
	port       int
}

func NewModule(config *configuration.Config) *CoreModule {
	module := CoreModule{}

	module.config = config
	module.ginRouter = gin.Default()

	return &module
}

func (c *CoreModule) Server() *gin.Engine {
	return c.ginRouter
}

func (c *CoreModule) Start() {

	fmt.Printf("Loading static resources from %s\n", c.config.Frontend.ResourcesPath)
	c.ginRouter.Use(static.Serve("/", static.LocalFile(c.config.Frontend.ResourcesPath, true)))

	fmt.Printf("Starting Server at requested port %s\n", c.config.Server.Port)

	listener, err := net.Listen("tcp", c.config.Server.Port)
	if err != nil {
		log.Fatal("Listen:", err)
	}

	c.port = listener.Addr().(*net.TCPAddr).Port

	c.httpServer = &http.Server{
		Handler: c.ginRouter,
	}

	fmt.Printf("Starting Server at real port %d\n", c.port)

	c.httpServer.Serve(listener)

}

func (c *CoreModule) ServerPort() int {
	return c.port
}

func (c *CoreModule) Stop() {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := c.httpServer.Shutdown(ctx); err != nil {
		log.Fatal("Server Shutdown:", err)
	}
}
