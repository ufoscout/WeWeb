#!/bin/bash

PROJECT_NAME=rust_actix
BUILD_TARGET_PATH=./target
BUILD_TARGET_OS=x86_64-unknown-linux-musl
BUILD_COPY_TO=./target/build

declare -a steps=(
  "rm -rf ${BUILD_COPY_TO}"
  "cargo test"
  "cargo build --release --target=${BUILD_TARGET_OS}"
  "strip ${BUILD_TARGET_PATH}/${BUILD_TARGET_OS}/release/${PROJECT_NAME}"
  "mkdir ${BUILD_COPY_TO}"
  "cp -r ${BUILD_TARGET_PATH}/${BUILD_TARGET_OS}/release/${PROJECT_NAME} ${BUILD_COPY_TO}"
  "cp -r ./config ${BUILD_COPY_TO}"
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
