#!/bin/bash

PROJECT_NAME=Go-Chi
BUILD_TARGET_PATH=./target
BUILD_TARGET_FILE=go

declare -a steps=(
  "rm -rf ${BUILD_TARGET_PATH}"
  "GOCACHE=off vgo test ./..."
  "CGO_ENABLED=0 vgo build -o ${BUILD_TARGET_PATH}/${BUILD_TARGET_FILE}"
  "cp -a ./config ${BUILD_TARGET_PATH}/config"
  "cp -a ../../frontend/Angular-Typescript/dist/Angular-Typescript ${BUILD_TARGET_PATH}/frontend"
)

echo $LINE_SEPARATOR
echo "Build $PROJECT_NAME"
echo $LINE_SEPARATOR

for i in "${steps[@]}"
do
    echo "Execute step: '$i'"
    eval $i
    rc=$?
    if [[ $rc -ne 0 ]] ; then
        echo "Failure executing: $i"; exit $rc
    fi
done
