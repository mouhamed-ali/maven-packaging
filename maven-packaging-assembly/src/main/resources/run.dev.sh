#!/bin/bash
echo -----------------------------------------------------------------
echo                Start packaging
echo -----------------------------------------------------------------

cd ../../.. && mvn clean package

if [ "$?" -ne "0" ]; then
  echo "Sorry, a maven error has occured"
  exit 1
fi

echo -----------------------------------------------------------------
echo                Start running the application
echo -----------------------------------------------------------------

PROJECT_NAME=assembly-packaging
PROJECT_VERSION=1.0-SNAPSHOT

echo PROJECT_NAME : $PROJECT_NAME
echo PROJECT_VERSION : $PROJECT_VERSION

pwd
cd ./target

DELIVERY_FILE=$(eval ls $PROJECT_NAME-$PROJECT_VERSION*.tar.gz)

TIMESTAMP="${DELIVERY_FILE:32:15}"


echo DELIVERY_FILE: $DELIVERY_FILE
echo TIMESTAMP: $TIMESTAMP


echo -----------------------------------------------------------------

tar -xvzf $DELIVERY_FILE && \
    cd $PROJECT_NAME-$PROJECT_VERSION-$TIMESTAMP && \
    cd $PROJECT_NAME && \
    cd $PROJECT_VERSION && \
    cd bin && \
    chmod -R +x . && \
    ls -ltr && \
    sh start.sh