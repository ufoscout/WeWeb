FROM alpine:3.14

RUN apk add --update --no-cache bash && rm -rf /var/cache/apk/*

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait

ENV FILE_NAME rust_actix
ENV WRK_DIR /opt/build

COPY ./target/build $WRK_DIR

EXPOSE 8080

WORKDIR $WRK_DIR

RUN chmod +x ./$FILE_NAME

CMD /wait && ./$FILE_NAME
