#!/bin/bash
PREFIX=$1

if [ PREFIX == "" ]
then
	PREFIX = "ldf"
fi

CONTAINER_NAME=$PREFIX-kos
WORKSPACE="/docker-workspace/kubik/kos-server"
KUBIK_DATA_DIR="/docker-volumes/$CONTAINER_NAME"

docker rm -f $CONTAINER_NAME

echo "Launching maven build for Kos."

mvn -f $WORKSPACE/pom.xml clean install

echo "Building Dockerfile for Kos."

sh $WORKSPACE/docker-resources/scripts/kos-server/docker-build.sh

CMD="docker run -d -p 8090:8080 -v $KUBIK_DATA_DIR:/data --name $CONTAINER_NAME cspinformatique/kos"

echo "Launching docker instance for Kos."
echo "	Cmd : $CMD"

$CMD
