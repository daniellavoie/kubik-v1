FROM java:openjdk-8-alpine

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

EXPOSE 8080

RUN ["apk", "add", "--update", "ttf-dejavu"]

CMD ["java", "-jar", "/kubik/kubik.jar"]

ADD kubik-server/target/kubik.jar /kubik/kubik.jar
