#!/bin/bash
WORKSPACE="/docker-workspace/ldf-kubik"
KUBIK_DATA_DIR="/docker-volumes/ldf-kubik"

docker rm -f ldf-kubik

cd $WORKSPACE

echo "Launching maven build for Kubik."

mvn clean install

cd docker-resources/scripts

echo "Building Dockerfile for Kubik."

sh docker-build.sh

CMD="docker run -d -p 8090:8080 -v $KUBIK_DATA_DIR:/data --name ldf-kubik cspinformatique/kubik"

echo "Launching docker instance for Kubik."
echo "	Cmd : $CMD"

$CMD
