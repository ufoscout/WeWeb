LINE_SEPARATOR='--------------------------------------------------------'

echo $LINE_SEPARATOR
echo 'Build Frontends'
echo $LINE_SEPARATOR

LINE_SEPARATOR='--------------------------------------------------------'

cd Angular-Typescript
./build.sh
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; cd ..; exit $rc
fi
cd ..
