FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "tpAchatProject-0.0.1-SNAPSHOT.jar"]
