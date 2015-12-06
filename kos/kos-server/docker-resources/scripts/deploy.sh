#!/bin/bash
PREFIX=$1
SCRIPTS_FOLDER="/docker-workspace/Kubik/server/docker-resources/scripts"

if [ PREFIX == "" ]
then
	PREFIX = "ldf"
fi

cd $SCRIPTS_FOLDER

elasticsearch/deploy.sh $PREFIX
mysql/deploy.sh $PREFIX
kubik/deploy.sh $PREFIX
