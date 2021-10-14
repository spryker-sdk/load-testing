#!/bin/bash

CURRENT_DIR="$( cd "$(dirname "$0")" ; pwd -P )"
ROOT_DIR="$( cd "${CURRENT_DIR}/../" ; pwd -P )"

DIST_FOLDER=$ROOT_DIR/.dist
TARGET_DIR=$ROOT_DIR/gatling

mkdir -p ${DIST_FOLDER}

GATLING_DOWNLOAD_MAVEN_URL='https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.3.0/gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
GATLINT_ARCHIVE_NAME='gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
GATLINT_DIRECTORY_NAME='gatling-charts-highcharts-bundle-3.3.0'

rm -rf ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME}
rm -rf ${TARGET_DIR}

curl ${GATLING_DOWNLOAD_MAVEN_URL} --output ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME}
unzip -q ${DIST_FOLDER}/${GATLINT_ARCHIVE_NAME} -d ${DIST_FOLDER}
mv ${DIST_FOLDER}/$GATLINT_DIRECTORY_NAME ${TARGET_DIR}

npm install --production
