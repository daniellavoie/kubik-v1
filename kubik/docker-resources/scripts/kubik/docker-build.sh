WORKSPACE="/docker-workspace/Kubik/kubik"

unzip -o $WORKSPACE/kubik-server/target/docker.zip -d $WORKSPACE/kubik-server/target

docker build -t cspinformatique/kubik $WORKSPACE/kubik-server/target/docker
