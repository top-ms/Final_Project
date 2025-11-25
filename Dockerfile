FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre

LABEL authors="mykhailosavchuk"
LABEL description="Appliance Store Spring Boot Application"

WORKDIR /app


RUN groupadd -r spring && useradd -r -g spring spring

USER spring:spring

COPY --from=build /build/target/Appliance-store-Spring-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "app.jar"]