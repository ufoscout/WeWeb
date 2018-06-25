FROM alpine:3.7

RUN apk add --update --no-cache bash && rm -rf /var/cache/apk/*

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

ENV TARGET_PATH ./target
ENV FILE_NAME go

ENV WRK_DIR /opt/build

COPY $TARGET_PATH $WRK_DIR

EXPOSE 8080
ENV GIN_MODE release

WORKDIR $WRK_DIR

RUN chmod +x ./$FILE_NAME

CMD /wait && ./$FILE_NAME
