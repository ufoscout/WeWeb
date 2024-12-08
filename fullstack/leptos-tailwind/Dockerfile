FROM alpine:3

RUN apk add --update --no-cache bash && rm -rf /var/cache/apk/*

COPY --from=ghcr.io/ufoscout/docker-compose-wait:latest /wait /wait

ENV FILE_NAME leptos_tailwind
ENV WRK_DIR /app

ENV RUST_LOG="info"
ENV LEPTOS_SITE_ADDR="0.0.0.0:8080"
ENV LEPTOS_SITE_ROOT=./site

COPY ./target/site $WRK_DIR/site
COPY ./target/x86_64-unknown-linux-musl/release/$FILE_NAME $WRK_DIR

EXPOSE 8080

WORKDIR $WRK_DIR

RUN chmod +x ./$FILE_NAME

# Set stop signal to ctrl+c
STOPSIGNAL SIGINT

CMD /wait && ./$FILE_NAME
