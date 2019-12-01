FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

FROM openjdk:8-jre-alpine

WORKDIR /app
EXPOSE 8080

COPY --from=MAVEN_BUILD /build/target/homework-server-v1.jar /app/
CMD ["java", "-jar", "homework-server-v1.jar"]