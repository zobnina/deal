FROM openjdk:17-alpine
RUN mkdir /app
WORKDIR /app
COPY /build/libs/$APP_NAME /app/$APP_NAME
ENTRYPOINT java -jar /app/$APP_NAME
EXPOSE $SERVER_PORT