FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk apk update && apk add --no-cache bash npm curl git unzip g++ gcc libgcc libstdc++ linux-headers make

COPY . .

ARG PORT=3000
ARG HOST=0.0.0.0
ARG APP_ENV=production
ARG DIST_FOLDER=./.dist
ARG TARGET_DIR=./gatling

ENV NODE_ENV=${APP_ENV}
ENV PORT=${PORT}
ENV HOST=${HOST}

RUN mkdir -p ${DIST_FOLDER}

ARG GATLING_DOWNLOAD_MAVEN_URL='https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.3.0/gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
ARG GATLINT_ARCHIVE_NAME='gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
ARG GATLINT_DIRECTORY_NAME='gatling-charts-highcharts-bundle-3.3.0'

RUN rm -rf  ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME}
RUN rm -rf  ${TARGET_DIR}

RUN curl ${GATLING_DOWNLOAD_MAVEN_URL} --output ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME}
RUN unzip -q ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME} -d ${DIST_FOLDER}
RUN mv ${DIST_FOLDER}/${GATLINT_DIRECTORY_NAME} ${TARGET_DIR}

RUN chmod a+x ./entrypoint.sh

RUN npm install --production

EXPOSE ${PORT}/tcp

ENTRYPOINT ["./entrypoint.sh"]
