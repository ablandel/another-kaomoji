# Build inspired by https://zwbetz.com/why-is-my-gradle-build-in-docker-so-slow/ article.
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /home/gradle/src
# Only copy dependency-related files.
COPY build.gradle.kts gradle.properties settings.gradle.kts ./
# Only download dependencies. The dependencies will be cached for next builds.
# Eat the expected build failure since no source code has been copied yet.
RUN gradle clean build --no-daemon > /dev/null 2>&1 || true
# Copy project sources.
COPY ./src ./src
# Do the real build without the unit tests (Docker would be required for DAOs tests).
RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/another-kaomoji-*.jar another-kaomoji.jar
ENTRYPOINT ["java", "-jar", "/app/another-kaomoji.jar"]
