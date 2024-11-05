FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/tpAchatProject-0.0.1.jar tpAchatProject-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/tpAchatProject-0.0.1.jar"]
