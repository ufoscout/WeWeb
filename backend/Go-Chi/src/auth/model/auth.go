package model

type Auth struct {
	Id         int64                   `json:"id"`
	Username   string                  `json:"username"`
	Roles      []string                `json:"roles"`
	Properties map[string]interface{}  `json:"properties"`
}

type Role struct {
	Id          int
	Name        string
	Permissions []string
}