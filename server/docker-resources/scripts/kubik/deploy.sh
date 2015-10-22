#!/bin/bash
PREFIX=$1

if [ PREFIX == "" ]
then
	PREFIX = "ldf"
fi

CONTAINER_NAME=$PREFIX-kubik
WORKSPACE="/docker-workspace/Kubik"
KUBIK_DATA_DIR="/docker-volumes/$CONTAINER_NAME"

docker rm -f $CONTAINER_NAME

echo "Launching maven build for Kubik."

mvn -f $WORKSPACE/pom.xml clean install

echo "Building Dockerfile for Kubik."

sh $WORKSPACE/server/docker-resources/scripts/kubik/docker-build.sh

CMD="docker run -d -p 8090:8080 -v $KUBIK_DATA_DIR:/data -v /etc/localtime:/etc/localtime:ro -v /etc/timezone:/etc/timezone:ro --name $CONTAINER_NAME cspinformatique/kubik"

echo "Launching docker instance for Kubik."
echo "	Cmd : $CMD"

$CMD
