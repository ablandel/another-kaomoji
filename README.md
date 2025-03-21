# Another Kaomoji ＼(٥⁀▽⁀ )／

Simple project to test Gradle + Kotlin + Spring Boot + GraphQL against a database.

### Start the project

A `docker-compose.yaml` file is provided to easier the developments. Another `PostgreSQL` instance can be used; in this
case the `application.yaml` may have to be updated.

```shell
docker compose up
```

This project is also configured with the `spring-boot-docker-compose` Spring dependency. The `docker-compose.yaml` can
be automatically launched using `spring.docker.compose.enabled` property.

#### Start the server

Using the prod profile with a database already configured and running:

```shell
./gradlew bootRun
```

For developers with useful tools:

```shell
SPRING_PROFILES_ACTIVE=dev \
SPRING_DOCKER_COMPOSE_ENABLED=true \
LOGGING_LEVEL_WEB=info \
SPRING_MVC_LOG_REQUEST_DETAILS=true \
SPRING_CODEC_LOG_REQUEST_DETAILS=true \
SPRING_JPA_SHOW_SQL=true \
HIBERNATE_GENERATE_STATISTICS=false \
HIBERNATE_USE_SQL_COMMENTS=false \
./gradlew bootRun
```

### Run the tests

*Note: `Docker` is required to run the DAOs tests.*

#### Unit tests

```shell
./gradlew clean test
```

### Docker image build

A `local.docker-compose.yaml` file is provided to easier the test using Docker images. For the moment, the Docker image
are not pushed and need to be locally built.

#### Build the image

```shell
docker build -t another-kaomoji:local .
```

#### Start the docker compose
```shell
docker compose -f local.docker-compose.yaml up
```
