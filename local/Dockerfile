FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk apk update && apk add --no-cache bash npm curl git unzip g++ gcc libgcc libstdc++ linux-headers make && rm -rf /var/cache/apk/*

ENTRYPOINT ["tail", "-f", "/dev/null"]
