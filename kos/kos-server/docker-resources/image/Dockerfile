FROM dockerfile/java:oracle-java8

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

ADD kos-server.jar /kos/kos-server.jar
ADD run.sh /run.sh

RUN chmod +x /*.sh

VOLUME [/"data"]

EXPOSE 8080

CMD ["/run.sh"]