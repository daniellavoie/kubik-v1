FROM dockerfile/java:oracle-java8

MAINTAINER Daniel Lavoie <dlavoie@cspinformatiqe.com>

ADD dilicom-sync.jar /dilicom-sync/dilicom-sync.jar
ADD run.sh /run.sh

RUN chmod +x /*.sh

EXPOSE 8080

CMD ["/run.sh"]
