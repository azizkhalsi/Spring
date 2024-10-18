FROM openjdk:17-jdk-alpine
ENV SERVER_PORT=8089
EXPOSE 8081
COPY target/spring.jar /5ds3-g2-spring.jar
ENTRYPOINT ["java" , "-jar" , "/5ds3-g2-spring.jar"]
