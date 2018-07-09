#!/bin/bash

LINE_SEPARATOR='--------------------------------------------------------'

echo $LINE_SEPARATOR
echo 'Build Backends'
echo $LINE_SEPARATOR

declare -a projects=(
    "Go-Chi" 
    "SpringBoot2-Java" 
    "Vertx3-Kotlin"
)

for i in "${projects[@]}"
do
    LINE_SEPARATOR='--------------------------------------------------------'

    cd $i
    ./build.sh
    rc=$?
    if [[ $rc -ne 0 ]] ; then
        echo "Failure builind $i"; cd ..; exit $rc
    fi
    cd ..
done
