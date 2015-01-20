#!/bin/bash
WORKSPACE="/docker-workspace/ldf-kubik"
ES_DATA_DIR="/docker-volumes/ldf-kubik-elasticsearch"

docker rm -f ldf-kubik-elasticsearch

echo "Deploying Elasticsearch for Kubik."
mkdir -p $ES_DATA_DIR

CMD="cp $WORKSPACE/docker-resources/config/elasticsearch.yml $ES_DATA_DIR/elasticsearch.yml"

echo "Copying configuration file to mounted volume."
echo "	Cmd : $CMD"

$CMD

echo "Launching docker instance of Elasticsearch"

CMD="docker run --name=ldf-kubik-elasticsearch -d -p 9200:9200 -p 9300:9300 -e ES_HEAP_SIZE=3g -v $ES_DATA_DIR:/data dockerfile/elasticsearch /elasticsearch/bin/elasticsearch -Des.config=/data/elasticsearch.yml"

echo "	Cmd : $CMD"

$CMD
