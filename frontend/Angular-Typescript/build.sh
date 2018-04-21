
echo $LINE_SEPARATOR
echo 'Build Angular-Typescript'
echo $LINE_SEPARATOR

rm -rf dist
rm -rf node_modules

npm install
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi

npm run lint
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi

npm run test-single-run
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi

npm run build
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi
