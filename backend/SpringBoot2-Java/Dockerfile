FROM openjdk:8-jre-alpine

RUN apk add --update bash && rm -rf /var/cache/apk/*

#ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait
#RUN chmod +x /wait

ENV JAR_NAME weweb-springboot2-java.jar
ADD ./target/$JAR_NAME /opt/build/

EXPOSE 8080

WORKDIR /opt/build/
CMD java $JVM_ARGS -jar $JAR_NAME
