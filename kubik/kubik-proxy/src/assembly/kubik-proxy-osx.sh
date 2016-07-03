DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ENV="prod"
LOG_FILE="/var/log/kubik-proxy-$ENV.log"

sudo rm $LOG_FILE

java -jar "$DIR/kubik-proxy.jar" > $LOG_FILE 