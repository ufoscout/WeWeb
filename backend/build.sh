LINE_SEPARATOR='--------------------------------------------------------'

echo $LINE_SEPARATOR
echo 'Build Backends'
echo $LINE_SEPARATOR

LINE_SEPARATOR='--------------------------------------------------------'

cd SpringBoot2-Java
./build.sh
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; cd ..; exit $rc
fi
cd ..

LINE_SEPARATOR='--------------------------------------------------------'

cd Vertx3-Kotlin
./build.sh
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; cd ..; exit $rc
fi
cd ..

