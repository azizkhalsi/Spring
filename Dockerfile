FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/tpAchatProject-0.0.1.jar tpAchatProject-0.0.1.jar
<<<<<<< HEAD
ENTRYPOINT ["java", "-jar", "tpAchatProject-0.0.1.jar"]
=======
ENTRYPOINT ["java", "-jar", "/tpAchatProject-0.0.1.jar"]
>>>>>>> 0a29af2541fbf7c2f9af278c6ca4d07eed8bebe9
