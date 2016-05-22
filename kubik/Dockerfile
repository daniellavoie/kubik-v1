FROM java:openjdk-8u77-jre-alpine

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

EXPOSE 8080

CMD ["java", "-jar", "/kubik/kubik.jar"]

ADD kubik-server/target/kubik.jar /kubik/kubik.jar
