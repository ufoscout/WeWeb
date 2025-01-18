# Builds a docker image with the server binary and the site package.
[group('docker')]
docker_build: build
  docker compose build


# Start all the docker containers defined in the compose file
[group('docker')]
docker_up:
  docker compose up -d --build


# Stop all the docker containers defined in the compose file
[group('docker')]
docker_down:
  @# Stop services only:                     docker compose stop
  @# Stop and remove containers, networks..: docker compose down 
  @# Down and remove volumes:                docker compose down --volumes
  docker compose down --volumes
