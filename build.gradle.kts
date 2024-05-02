plugins {
    id("org.springframework.boot") version "3.2.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}

group = "github.ablandel.anotherkaomoji"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val springVersion = "3.2.5"
val testcontainersVersion = "1.19.7"

dependencies {
    implementation(kotlin("reflect:1.9.23"))
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-graphql:$springVersion")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.liquibase:liquibase-core:4.27.0")
    runtimeOnly("org.postgresql:postgresql:42.7.3")
    runtimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading") // see: https://eclipse.dev/openj9/docs/xxenabledynamicagentloading/
}
kotlin {
    jvmToolchain(21)
}