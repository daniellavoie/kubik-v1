FROM java:openjdk-8-alpine

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

EXPOSE 8080

# Ugly hack required since OpenJDK encounters an NPE on X11FontManager.createFontConfiguration.
RUN ["apk", "add", "--update", "ttf-dejavu"]

CMD ["java", "-jar", "/kubik/kubik.jar"]

ADD target/kubik.jar /kubik/kubik.jar
