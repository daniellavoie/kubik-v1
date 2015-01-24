#!/bin/bash
SCRIPTS_FOLDER="/docker-workspace/Kubik/server-docker-resources/scripts"

cd $SCRIPTS_FOLDER

elasticsearch/deploy.sh
mysql/deploy.sh
kubik/deploy.sh
