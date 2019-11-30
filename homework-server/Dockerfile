FROM openjdk:8-jre-alpine

WORKDIR /app
EXPOSE 8081
ADD ./target/homework-server.jar .
CMD ["java", "-jar", "homework-server.jar"]