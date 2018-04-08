LINE_SEPARATOR='--------------------------------------------------------'

## Backend
cd backend

echo $LINE_SEPARATOR
echo 'Build SpringBoot2-Java'
echo $LINE_SEPARATOR
cd SpringBoot2-Java
mvn clean verify
cd ..

echo $LINE_SEPARATOR
echo 'Build Vertx3-Kotlin'
echo $LINE_SEPARATOR
cd Vertx3-Kotlin
mvn clean verify
cd ..

cd ..

## Frontend
cd frontend

echo $LINE_SEPARATOR
echo 'Build Angular-Typescript'
echo $LINE_SEPARATOR
cd Angular-Typescript
rm -rf dist
rm -rf node_modules
npm install
npm run lint
npm run test-single-run
npm run build
cd ..

cd ..
