DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

INSTALL_FOLDER="/Applications/Kubik-Proxy/"

sudo rm -rf $INSTALL_FOLDER

sudo cp "$DIR/kubik-proxy.jar" "$INSTALL_FOLDER/kubik-proxy.jar"
sudo cp "$DIR/application.properties" "$INSTALL_FOLDER/kubik-proxy-osx.sh"  
sudo cp "$DIR/kubik-proxy-osx.sh" "$INSTALL_FOLDER/kubik-proxy-osx.sh"
