FROM ubuntu

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"

COPY . src/test/java/cucumbertest
WORKDIR src/test/java/cucumbertest

## JAVA MVN INSTALLATION
RUN #apt-get update && \
    apt-get -y install oracle-java8-installer || true && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/ && \
    rm -rf /var/cache/oracle-jdk8-installer;

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
RUN export MAVEN_HOME

ENTRYPOINT mvn clean test verify
