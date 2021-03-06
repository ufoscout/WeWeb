package testUtil

import (
	"fmt"
	"os"
	"path/filepath"
	s "strings"
					)

/*
MainFolderPath returns the folder where the go.mod file is stored.
This is useful to access project relative folders from unit tests
*/
func MainFolderPath() string {

	referenceFile := "go.mod"

	pwd, _ := os.Getwd()
	end := false

	count := 0

	for !end {
		joined := filepath.Join(pwd, referenceFile)
		_, err := os.Stat(joined)
		if err != nil {
			//fmt.Printf("File %s does not exists\n", joined)

			lastSeparator := s.LastIndex(pwd, string(os.PathSeparator))

			//fmt.Printf("last separator: %d\n", lastSeparator)

			parent := pwd[:lastSeparator]
			//fmt.Printf("parent: %s\n", parent)
			pwd = parent
		} else {
			fmt.Printf("File %s exists\n", joined)
			end = true
		}
		count = count + 1

		if count > 100 {
			end = true
		}
	}

	return pwd
}
