FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk apk update && apk add --no-cache bash npm curl git unzip g++ gcc libgcc libstdc++ linux-headers make python2

COPY . .

ENV NODE_ENV=production
ARG PORT=3000
ARG DIST_FOLDER=./.dist
ARG TARGET_DIR=./gatling

RUN npm install --production

RUN mkdir -p ${DIST_FOLDER}

ARG GATLING_DOWNLOAD_MAVEN_URL='https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.3.0/gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
ARG GATLINT_ARCHIVE_NAME='gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
ARG GATLINT_DIRECTORY_NAME='gatling-charts-highcharts-bundle-3.3.0'

RUN curl ${GATLING_DOWNLOAD_MAVEN_URL} --output ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME}
RUN unzip -q ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME} -d ${DIST_FOLDER}
RUN mv ${DIST_FOLDER}/$GATLINT_DIRECTORY_NAME ${TARGET_DIR}

RUN chmod a+x ./entrypoint.sh

EXPOSE ${PORT}/tcp

ENTRYPOINT ["./entrypoint.sh"]
