plugins {
    // https://plugins.gradle.org/plugin/org.springframework.boot
    id("org.springframework.boot") version "3.3.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.spring
    id("org.jetbrains.kotlin.plugin.spring") version "2.0.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.jpa
    id("org.jetbrains.kotlin.plugin.jpa") version "2.0.0"
}

group = "github.ablandel.anotherkaomoji"
version = "1.1.0"

repositories {
    mavenCentral()
}

val springVersion = "3.3.0"
val testcontainersVersion = "1.19.8"

dependencies {
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-graphql
    implementation("org.springframework.boot:spring-boot-starter-graphql:$springVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-docker-compose
    implementation("org.springframework.boot:spring-boot-docker-compose:$springVersion")
    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
    // https://mvnrepository.com/artifact/org.liquibase/liquibase-core
    implementation("org.liquibase:liquibase-core:4.28.0")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    runtimeOnly("org.postgresql:postgresql:42.7.3")
    // https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher
    runtimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    // https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    // https://mvnrepository.com/artifact/org.testcontainers/postgresql
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.3.0")
}

tasks.register("bootRunDev") {
    group = "application"
    description = "Runs this project as a Spring Boot application with the dev profile"
    doFirst {
        tasks.bootRun.configure {
            systemProperty("spring.profiles.active", "dev")
            systemProperty("spring.docker.compose.enabled", true)
            systemProperty("logging.level.web", "info")
            systemProperty("spring.mvc.log-request-details", true)
            systemProperty("spring.codec.log-request-details", true)
        }
    }
    finalizedBy("bootRun")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading") // see: https://eclipse.dev/openj9/docs/xxenabledynamicagentloading/
}

kotlin {
    jvmToolchain(21)
}