#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

INSTALL_FOLDER="/Applications/Kubik-Proxy"

sudo rm -rf $INSTALL_FOLDER

mkdir $INSTALL_FOLDER
sudo cp "$DIR/distrib/kubik-proxy.jar" "$INSTALL_FOLDER/kubik-proxy.jar"
sudo cp "$DIR/distrib/application.properties" "$INSTALL_FOLDER/application.properties"
sudo cp "$DIR/distrib/kubik-proxy-osx.sh" "$INSTALL_FOLDER/kubik-proxy-osx.sh"

sudo cp "$DIR/distrib/com.cspinformatique.kubik.proxy.plist" "/Library/LaunchDaemons/com.cspinformatique.kubik.proxy.plist"

chmod 755 "$INSTALL_FOLDER/kubik-proxy-osx.sh"