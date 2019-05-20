FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/webspring.jar
ADD ${JAR_FILE} webspring.jar
ENTRYPOINT ["java","-jar","/webspring.jar"]
