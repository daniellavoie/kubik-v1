#!/bin/bash
PREFIX=$1

if [ PREFIX == "" ]
then
	PREFIX = "ldf"
fi

CONTAINER_NAME=$PREFIX-kubik-mysql
MYSQL_DATA_FOLDER=/docker-volumes/$CONTAINER_NAME

docker rm -f $CONTAINER_NAME

echo "Launching docker instance for mysql."

docker run -d --name $CONTAINER_NAME -p 3306:3306 -v $MYSQL_DATA_FOLDER:/var/lib/mysql dockerfile/mysql