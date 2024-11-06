FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/tpachatproject-0.0.1.jar tpachatproject-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/tpachatproject-0.0.1.jar"]
