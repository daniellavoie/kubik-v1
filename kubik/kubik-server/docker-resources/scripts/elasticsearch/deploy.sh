#!/bin/bash
PREFIX=$1

if [ PREFIX == "" ]
then
	PREFIX = "ldf"
fi

CONTAINER_NAME=$PREFIX-kubik-elasticsearch
WORKSPACE="/docker-workspace/Kubik/server"
ES_DATA_DIR="/docker-volumes/$CONTAINER_NAME"

docker rm -f $CONTAINER_NAME

echo "Deploying Elasticsearch for Kubik."
mkdir -p $ES_DATA_DIR

CMD="cp $WORKSPACE/docker-resources/config/elasticsearch.yml $ES_DATA_DIR/elasticsearch.yml"

echo "Copying configuration file to mounted volume."
echo "	Cmd : $CMD"

$CMD

echo "Launching docker instance of Elasticsearch"

CMD="docker run --name $CONTAINER_NAME -d -p 9200:9200 -p 9300:9300 -e ES_HEAP_SIZE=3g -v $ES_DATA_DIR:/data dockerfile/elasticsearch /elasticsearch/bin/elasticsearch -Des.config=/data/elasticsearch.yml"

echo "	Cmd : $CMD"

$CMD
