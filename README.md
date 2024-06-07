# Another Kaomoji ＼(٥⁀▽⁀ )／

Simple project to test Kotlin + Spring against a database.

### Start the project

A `docker-compose.yaml` file is provided to easier the developments. Another `PostgreSQL` instance can be used; in this
case the `application.yaml` may have to be updated.

```shell
docker compose up
```

This project is also configured with the `spring-boot-docker-compose` Spring dependency. The `docker-compose.yaml` can
be automatically launched using `spring.docker.compose.enabled` property or the gradle `bootRunDev` task.

#### Start the server

If a database is already running and the application configured.

```shell
./gradlew bootRun
```

Else, using the dev mode with the `spring-boot-docker-compose` Spring feature.

```shell
./gradlew bootRunDev
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
