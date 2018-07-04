package core

import (
	"fmt"
	"context"
	"log"
	"net/http"
	"time"
	"net"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/json"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/jwt"
	"github.com/go-chi/chi"
)

type Service struct {
	Config  *config.Config
	Router  *chi.Mux
	Json    *json.JsonService
	Jwt     *jwt.JwtService
}

type Module struct {
	Services   Service
	httpServer *http.Server
	port       int
}

func New(config *config.Config) *Module {
	module := Module{}

	jsonService := json.New()
	jwtService, err := jwt.New(config.Jwt, jsonService)
	if err!=nil {
		panic(err)
	}

	module.Services = Service{
		Config: config,
		Router: chi.NewRouter(),
		Json: jsonService,
		Jwt: jwtService,
	}

	return &module
}

func (core *Module) Init() error {
	return nil
}

func (core *Module) Start() error {

	if core.Services.Config.Frontend.Enabled {
		fmt.Printf("Loading static resources from %s\n", core.Services.Config.Frontend.ResourcesPath)

		//core.Services.Router.FileServer("/static", http.Dir("frontend"))

		/*
		core.Services.Router.Use(static.Serve("/", static.LocalFile(core.Services.Config.Frontend.ResourcesPath, true)))
		core.Services.Router.NoRoute(func(context *gin.Context){
			context.File(core.Services.Config.Frontend.ResourcesPath + "/index.html")
		})
		*/
	}

	return nil
}

func (core *Module) StartServer() {
	fmt.Printf("Starting Server at requested port %s\n", core.Services.Config.Server.Port)

	listener, err := net.Listen("tcp", ":" + core.Services.Config.Server.Port)
	if err != nil {
		panic(err)
	}

	core.port = listener.Addr().(*net.TCPAddr).Port

	core.httpServer = &http.Server{
		Handler: core.Services.Router,
	}

	fmt.Printf("Starting Server at real port %d\n", core.port)

	core.httpServer.Serve(listener)
}

func (core *Module) ServerPort() int {
	return core.port
}

func (core *Module) StopServer() {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := core.httpServer.Shutdown(ctx); err != nil {
		log.Fatal("Server Shutdown:", err)
	}
}
