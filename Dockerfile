FROM centos:centos7

MAINTAINER Adrian George <adrian.george@rackspace.com>

RUN yum update -y && \
  yum install -y java-1.8.0-openjdk-devel && \
  yum clean all

ENV JAVA_HOME /usr/lib/jvm/java
ENV GRADLE_USER_HOME=/opt/multicast-poc

COPY ./ /opt/multicast-poc

RUN chown -R 1001:0 /opt/multicast-poc && \
    chmod -R g=u /opt/multicast-poc

WORKDIR /opt/multicast-poc

RUN /opt/multicast-poc/gradlew build

USER 1001

EXPOSE 8080 9999/UDP

CMD ["java", "-jar", "/opt/multicast-poc/build/libs/multicast-poc-0.0.1-SNAPSHOT.jar"]
