FROM alpine:latest

ARG DEBIAN_FRONTEND=noninteractive

WORKDIR /jenkins

USER root

RUN apk update -y && apk upgrade -y --available
RUN apk add buildah openjdk17-jdk maven
RUN curl -L https://bintray.com/artifact/download/groovy/maven/apache-groovy-binary-2.4.8.zip -o /tmp/groovy.zip && \
    cd /usr/local && \
    unzip /tmp/groovy.zip && \
    rm /tmp/groovy.zip && \
    ln -s /usr/local/groovy-2.4.8 groovy && \
    /usr/local/groovy/bin/groovy -v && \
    cd /usr/local/bin && \
    ln -s /usr/local/groovy/bin/groovy groovy

USER jenkins
