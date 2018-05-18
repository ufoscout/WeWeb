#!/bin/bash

echo $LINE_SEPARATOR
echo 'Build Vertx3-Kotlin'
echo $LINE_SEPARATOR

mvn clean verify
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi


