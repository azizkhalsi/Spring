FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/tpAchatProject-0.0.1-SNAPSHOT.jar tpAchatProject-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "tpAchatProject-0.0.1-SNAPSHOT.jar"]
