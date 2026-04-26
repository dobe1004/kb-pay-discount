FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN apt-get update -q && \
    apt-get install -y wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip && \
    ./gradle-8.5/bin/gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
