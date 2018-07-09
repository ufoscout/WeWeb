package json

import (
		goJson "encoding/json"
)

type JsonService struct {

}

func New() *JsonService {
	return &JsonService{}
}

func (json *JsonService) ToJson(v interface{}) ([]byte, error) {
	return goJson.Marshal(v)
}

func (json *JsonService) FromJson(data []byte, v interface{}) error {
	return goJson.Unmarshal(data, v)
}