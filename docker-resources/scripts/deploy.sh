#!/bin/bash
SCRIPTS_FOLDER="/docker-workspace/ldf-kubik/docker-resources/scripts"

cd $SCRIPTS_FOLDER

elasticsearch/deploy.sh
mysql/deploy.sh
ldf-kubik/deploy.sh
