FROM java:openjdk-8u77-jre-alpine

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

ADD kubik/kubik-server/target/kubik.jar /kubik/kubik.jar

EXPOSE 8080

CMD ["java", "-jar", "/kubik/kubik.jar"]
