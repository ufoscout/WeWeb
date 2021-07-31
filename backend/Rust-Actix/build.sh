#!/bin/bash

PROJECT_NAME=rust_actix
BUILD_TARGET_PATH=./target
BUILD_TARGET_OS=x86_64-unknown-linux-musl
BUILD_COPY_TO=./target/build

BUILD_MUSL_STATIC_CMD="cargo build --release --target=${BUILD_TARGET_OS}"

RUST_VERSION=1.27.0
BUILD_MUSL_STATIC_CMD_WITH_DOCKER="
      docker run --rm -it -v $(pwd):/home/rust/src \
      -v $(pwd)/target/rust-musl-builder/cargo-git:/home/rust/.cargo/git \
      -v $(pwd)/target/rust-musl-builder/cargo-registry:/home/rust/.cargo/registry \
      ekidd/rust-musl-builder:${RUST_VERSION} \
      /bin/bash -c \"sudo chown -R rust:rust /home/rust/.cargo/git /home/rust/.cargo/registry && cargo build --release\"
      "

declare -a steps=(
  "rm -rf ${BUILD_COPY_TO}"
  "cargo test"
  "${BUILD_MUSL_STATIC_CMD}"
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
