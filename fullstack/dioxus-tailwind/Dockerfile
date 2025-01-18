# FROM alpine:3

# RUN apk add --update --no-cache bash && rm -rf /var/cache/apk/*

FROM ubuntu:24.04

COPY --from=ghcr.io/ufoscout/docker-compose-wait:latest /wait /wait

ENV FILE_NAME server
ENV WRK_DIR /app

COPY ./target/dx/web/release/web $WRK_DIR

EXPOSE 8080

WORKDIR $WRK_DIR

RUN chmod +x ./$FILE_NAME

# Set stop signal to ctrl+c
STOPSIGNAL SIGINT

ENV IP 0.0.0.0
ENV PORT 8080

CMD /wait && ./$FILE_NAME
