FROM openjdk:8-jdk-alpine
VOLUME /tmp

ARG JAR_FILE=target/webSpring.jar
ADD ${JAR_FILE} webSpring.jar
ENTRYPOINT ["java","-jar","/webSpring.jar"]
