# Another Kaomoji ＼(٥⁀▽⁀ )／

Simple project to test Kotlin + Spring project against a database.

### Start the project

A `docker-compose.yaml` file is provided to easier the developments. Another `PostgreSQL` instance can be used; in this
case the `application.yaml` may have to be updated.

#### Start the database

```shell
docker compose up
```

#### Start the server

*Note: A database is required to run the server. The `docker-compose.yaml` file can be used for dev purpose.
The `application.yaml` and the `docker-compose.yaml` files has been configured to be directly runnable after a fresh
clone.*

```shell
./gradlew bootRun
```

### Run the tests

*Note: `Docker` is required to run the DAOs tests.*

#### Unit tests

```shell
./gradlew clean test
```