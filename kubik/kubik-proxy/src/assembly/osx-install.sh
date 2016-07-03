#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

INSTALL_FOLDER="/Applications/Kubik-Proxy"

sudo rm -rf $INSTALL_FOLDER

sudo mkdir $INSTALL_FOLDER
sudo cp "$DIR/distrib/kubik-proxy.jar" "$INSTALL_FOLDER/kubik-proxy.jar"
sudo cp "$DIR/distrib/application.properties" "$INSTALL_FOLDER/application.properties"