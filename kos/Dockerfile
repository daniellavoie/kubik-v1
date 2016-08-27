FROM java:openjdk-8-alpine

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

EXPOSE 8080

# Ugly hack required since OpenJDK encounters an NPE on X11FontManager.createFontConfiguration.
RUN ["apk", "add", "--update", "ttf-dejavu"]

CMD ["java", "-jar", "/kos/kos-server.jar"]

ADD kos-server/target/kos-server.jar /kos/kos-server.jar
