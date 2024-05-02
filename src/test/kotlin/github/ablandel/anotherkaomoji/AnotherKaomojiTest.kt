package github.ablandel.anotherkaomoji

import github.ablandel.anotherkaomoji.test.util.PostgreSQLContainerInitializer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [PostgreSQLContainerInitializer::class])
class AnotherKaomojiTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `check the server starts after loading the complete stack`() {
        mockMvc.perform(
            get("/actuator/health")
        )
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"))
            .andExpect(jsonPath("$.status").value("UP"))
    }
}