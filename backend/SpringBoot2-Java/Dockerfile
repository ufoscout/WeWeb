# Application Builder image
FROM maven:3-jdk-8-slim as builder

ENV SRC /src
ADD ./ $SRC
WORKDIR $SRC

RUN chmod +x ./build.sh
RUN ./build.sh

# Build application image
FROM openjdk:8-jre-alpine

RUN apk add --update bash && rm -rf /var/cache/apk/*

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.0/wait /wait
RUN chmod +x /wait

ENV JAR_PATH target
ENV JAR_NAME weweb-springboot2-java.jar

ENV WRK_DIR = /opt/build/

COPY --from=builder /src/$JAR_PATH/$JAR_NAME $WRK_DIR

EXPOSE 8080

WORKDIR $WRK_DIR
CMD /wait && java $JVM_ARGS -jar $JAR_NAME
