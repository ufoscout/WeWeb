#!/bin/bash

LINE_SEPARATOR='--------------------------------------------------------'

echo $LINE_SEPARATOR
echo 'Build Frontends'
echo $LINE_SEPARATOR

declare -a projects=(
    "Angular-Typescript"
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
