#!/bin/bash

PROJECT_NAME=Go-GinGonic
BUILD_TARGET_PATH=./target
BUILD_TARGET_FILE=go

declare -a steps=(
  "rm -rf ${BUILD_TARGET_PATH}"
  "vgo test ./..."
  "vgo build -o ${BUILD_TARGET_PATH}/${BUILD_TARGET_FILE}"
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
