## Backend
cd backend

cd SpringBoot2-Java
mvn clean verify
cd ..

cd Vertx3-Kotlin
mvn clean verify
cd ..

cd ..

## Frontend
cd frontend

cd Angular-Typescript
npm run test-single-run
npm run build
cd ..

cd ..
