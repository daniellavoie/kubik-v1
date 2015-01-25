#!/bin/bash
MYSQL_DATA_FOLDER=/docker-volumes/ldf-mysql/

docker rm -f ldf-kubik-mysql

echo "Launching docker instance for mysql."

docker run -d --name ldf-kubik-mysql -p 3306:3306 -v $MYSQL_DATA_FOLDER:/var/lib/mysql/ dockerfile/mysql