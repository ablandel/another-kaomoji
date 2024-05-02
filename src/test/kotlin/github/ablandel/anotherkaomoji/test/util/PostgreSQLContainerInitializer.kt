package github.ablandel.anotherkaomoji.test.util

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class PostgreSQLContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        @Container
        val container: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:16-alpine").apply {
            start()
        }
    }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=" + container.jdbcUrl,
            "spring.datasource.username=" + container.username,
            "spring.datasource.password=" + container.password
        ).applyTo(configurableApplicationContext.environment)
    }
}