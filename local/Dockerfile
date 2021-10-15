FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk apk update && apk add --no-cache bash npm curl git unzip g++ gcc libgcc libstdc++ linux-headers make

ENTRYPOINT ["tail", "-f", "/dev/null"]
