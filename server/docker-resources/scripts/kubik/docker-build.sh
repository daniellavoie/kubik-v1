WORKSPACE="/docker-workspace/Kubik"

unzip -o $WORKSPACE/server/target/docker.zip -d $WORKSPACE/server/target

docker build -t cspinformatique/kubik $WORKSPACE/server/target/docker
