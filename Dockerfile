#FROM openjdk:17-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ./target/tpAchatProject-0.0.1-SNAPSHOT 5DS3-G4-TPACHAT.jar
#ENTRYPOINT ["java" , "-jar" , "/app.jar"]


#FROM openjdk:17-jdk-alpine
#ARG JAR_FILE=target/tpAchatBuild.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java" , "-jar" , "/app.jar"]


FROM openjdk:17-jdk-alpine

ENV NEXUS_USERNAME=admin
ENV NEXUS_PASSWORD=KH@moha001

WORKDIR /app

EXPOSE 8089

RUN apk add --no-cache curl \
    && curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -O http://192.168.154.130:8081/repository/springproject/com/projet/tpAchatProject/0.0.1-SNAPSHOT/tpAchatProject-0.0.1-20241027.114618-1.jar

ENTRYPOINT ["java", "-jar", "tpAchatProject-0.0.1-20241027.114618-1.jar"]
