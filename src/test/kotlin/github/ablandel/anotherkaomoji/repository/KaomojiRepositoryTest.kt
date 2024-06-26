package github.ablandel.anotherkaomoji.repository

import github.ablandel.anotherkaomoji.entity.Kaomoji
import github.ablandel.anotherkaomoji.test.util.PostgreSQLContainerInitializer
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [PostgreSQLContainerInitializer::class])

class KaomojiRepositoryTest {

    @Autowired
    private lateinit var kaomojiRepository: KaomojiRepository

    @Test
    @Transactional
    @Rollback
    fun `test findWithLimitAndOffset`() {
        kaomojiRepository.save(
            Kaomoji(
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key2",
                emoticon = "emoticon2",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key3",
                emoticon = "emoticon3",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key4",
                emoticon = "emoticon4",
                tags = listOf()
            )
        )
        val kaomojis = kaomojiRepository.findWithLimitAndOffset(1, 2)
        assertTrue(kaomojis.size == 2)
        assertTrue(kaomojis.all { kaomoji -> kaomoji.id != null })
        assertTrue(kaomojis.all { kaomoji -> kaomoji.createdAt != null })
        assertTrue(kaomojis.all { kaomoji -> kaomoji.updatedAt != null })
        assertTrue(kaomojis.map { kaomoji: Kaomoji -> kaomoji.key }.toList().containsAll(listOf("key2", "key3")))

        val kaomojisWithCursor = kaomojiRepository.findWithLimitAndCursor(1L, 2)
        assertTrue(kaomojisWithCursor.size == 2)
        assertTrue(kaomojisWithCursor.all { kaomoji -> kaomoji.id != null && kaomoji.id!! > 1L })
        assertTrue(kaomojisWithCursor.all { kaomoji -> kaomoji.createdAt != null })
        assertTrue(kaomojisWithCursor.all { kaomoji -> kaomoji.updatedAt != null })
    }

    @Test
    @Transactional
    @Rollback
    fun `test findByKeyOrEmoticon`() {
        kaomojiRepository.save(
            Kaomoji(
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key2",
                emoticon = "emoticon2",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key3",
                emoticon = "emoticon3",
                tags = listOf()
            )
        )
        kaomojiRepository.save(
            Kaomoji(
                key = "key4",
                emoticon = "emoticon4",
                tags = listOf()
            )
        )
        val kaomojis = kaomojiRepository.findByKeyOrEmoticon("Key2", "emoticon3")
        assertTrue(kaomojis.size == 2)
        assertTrue(kaomojis.all { kaomoji -> kaomoji.id != null })
        assertTrue(kaomojis.all { kaomoji -> kaomoji.createdAt != null })
        assertTrue(kaomojis.all { kaomoji -> kaomoji.updatedAt != null })
        assertTrue(kaomojis.map { kaomoji: Kaomoji -> kaomoji.key }.toList().containsAll(listOf("key2", "key3")))
    }

    @Test
    @Transactional
    @Rollback
    fun `test findByLabelIgnoreCase`() {
        kaomojiRepository.save(
            Kaomoji(
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf()
            )
        )
        val kaomoji = kaomojiRepository.findByKeyIgnoreCase("key1")
        assertTrue(kaomoji.isPresent)
        assertTrue(kaomoji.get().id != null)
        assertTrue(kaomoji.get().createdAt != null)
        assertTrue(kaomoji.get().updatedAt != null)
        assertTrue(kaomoji.get().key == "key1")
        assertTrue(kaomoji.get().emoticon == "emoticon1")
        assertTrue(kaomoji.get().tags.isEmpty())
        val kaomojiOtherCase = kaomojiRepository.findByKeyIgnoreCase("Key1")
        assertTrue(kaomojiOtherCase.isPresent)
        assertTrue(kaomojiOtherCase.get().id != null)
        assertTrue(kaomojiOtherCase.get().createdAt != null)
        assertTrue(kaomojiOtherCase.get().updatedAt != null)
        assertTrue(kaomojiOtherCase.get().key == "key1")
        assertTrue(kaomojiOtherCase.get().emoticon == "emoticon1")
        assertTrue(kaomojiOtherCase.get().tags.isEmpty())
    }

    @Test
    @Transactional
    @Rollback
    fun `test findByEmoticon`() {
        kaomojiRepository.save(
            Kaomoji(
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf()
            )
        )
        val kaomoji = kaomojiRepository.findByEmoticon("emoticon1")
        assertTrue(kaomoji.isPresent)
        assertTrue(kaomoji.get().id != null)
        assertTrue(kaomoji.get().createdAt != null)
        assertTrue(kaomoji.get().updatedAt != null)
        assertTrue(kaomoji.get().key == "key1")
        assertTrue(kaomoji.get().emoticon == "emoticon1")
        assertTrue(kaomoji.get().tags.isEmpty())
    }
}