#FROM openjdk:17-jdk-alpine
#ENV SERVER_PORT=8089
#EXPOSE 8081
#COPY target/tpAchatBuild.jar /5ds3-g2-tpachat.jar
#ENTRYPOINT ["java" , "-jar" , "/5ds3-g2-tpachat.jar"]

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/tpAchatBuild.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java" , "-jar" , "/app.jar"]