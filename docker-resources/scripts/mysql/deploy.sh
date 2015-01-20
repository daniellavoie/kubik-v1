#!/bin/bash

docker rm -f ldf-kubik-mysql

echo "Launching docker instance for mysql."

docker run -d --name ldf-kubik-mysql -p 3306:3306 dockerfile/mysql