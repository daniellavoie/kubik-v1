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

cd $WORKSPACE

echo "Launching maven build for Kubik."

mvn clean install

cd docker-resources/scripts

echo "Building Dockerfile for Kubik."

sh $WORKSPACE/server/docker-build.sh

CMD="docker run -d -p 8090:8080 -v $KUBIK_DATA_DIR:/data --name $CONTAINER_NAME cspinformatique/kubik"

echo "Launching docker instance for Kubik."
echo "	Cmd : $CMD"

$CMD
