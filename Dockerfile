FROM amazoncorretto:17.0.17-alpine3.22

ENV APP_NAME=RetinaCareBackend
WORKDIR /app

COPY target/${APP_NAME}.jar /app/${APP_NAME}.jar

EXPOSE 8080
CMD java -jar /app/${APP_NAME}.jar