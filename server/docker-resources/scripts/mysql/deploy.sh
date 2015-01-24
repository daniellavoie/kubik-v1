#!/bin/bash
MYSQL_DATA_FOLDER=/docker-volumes/ldf-kubik-mysql/data
MYSQL_CONF_FOLDER=/docker-volumes/ldf-kubik-mysql/conf

docker rm -f ldf-kubik-mysql

echo "Launching docker instance for mysql."

docker run -d --name ldf-kubik-mysql -p 3306:3306 -v $MYSQL_DATA_FOLDER:/var/lib/mysql -v $MYSQL_CONF_FOLDER:/etc/mysql dockerfile/mysql
