PROJECT_NAME=Angular-Typescript

declare -a steps=(
  "rm -rf dist"
  "rm -rf node_modules"
  "npm install"
  "npm run lint"
  "run test-single-run"
  "npm run build"
)

echo $LINE_SEPARATOR
echo "Build $PROJECT_NAME"
echo $LINE_SEPARATOR

for i in "${steps[@]}"
do
    eval $i
    rc=$?
    if [[ $rc -ne 0 ]] ; then
        echo "Failure executing: $i"; exit $rc
    fi
done
