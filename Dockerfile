FROM openjdk:17-jdk-alpine
MAINTAINER baeldung.com
COPY target/credit-0.0.1-SNAPSHOT.jar credit-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/credit-0.0.1-SNAPSHOT.jar"]