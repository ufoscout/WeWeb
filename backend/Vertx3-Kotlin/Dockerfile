FROM openjdk:8-jre-alpine

RUN apk add --update bash && rm -rf /var/cache/apk/*

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.3.0/wait /wait
RUN chmod +x /wait

ENV JAR_PATH ./target
ENV JAR_NAME weweb-vertx3-kotlin-fat.jar

ENV WRK_DIR /opt/build/

COPY $JAR_PATH/$JAR_NAME $WRK_DIR

EXPOSE 8080

WORKDIR $WRK_DIR
CMD /wait && java $JVM_ARGS -jar $JAR_NAME
