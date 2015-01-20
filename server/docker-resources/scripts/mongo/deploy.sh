#!/bin/bash
MONGO_DATA_DIR="/docker-volumes/ldf-mongodb/data"
MONGO_LOGS_DIR="/docker-volumes/ldf-mongodb/syslog"

docker rm -f ldf-mongodb

echo "Launching docker instance for mongodb."

docker run -d -p 27017:27017 --name ldf-mongodb -v $MONGO_LOGS_DIR:/var/log -v $MONGO_DATA_DIR:/data cspinformatique/mongodb