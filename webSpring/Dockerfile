FROM openjdk:8-jdk-alpine
VOLUME /tmp

ARG JAR_FILE=target/webspring-img.jar
ADD ${JAR_FILE} webspring-img.jar
ENTRYPOINT ["java","-jar","/webspring-img.jar"]
