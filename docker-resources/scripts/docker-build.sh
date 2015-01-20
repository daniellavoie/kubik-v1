unzip -o ../../target/docker.zip -d ../target

docker build -t cspinformatique/kubik ../target/docker
