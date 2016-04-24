FROM jenkins

# Switch user to root so that we can install apps (jenkins image switches to user "jenkins" in the end)
USER root

# Install Docker prerequisites
RUN apt-get update -qq && apt-get install -qqy \
    apt-transport-https \
    apparmor \
    ca-certificates \
        lxc \
        supervisor

# Install Docker from Docker Inc. repositories.
RUN echo deb https://apt.dockerproject.org/repo debian-jessie main > /etc/apt/sources.list.d/docker.list \
  && apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D \
  && apt-get update -qq \
  && apt-get install -qqy docker-engine