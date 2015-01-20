FROM dockerfile/java:oracle-java8

MAINTAINER Daniel Lavoie <dlavoie@cspinformatique.com>

ADD kubik.jar /kubik/kubik.jar
ADD run.sh /run.sh

RUN chmod +x /*.sh

EXPOSE 8080

CMD ["/run.sh"]