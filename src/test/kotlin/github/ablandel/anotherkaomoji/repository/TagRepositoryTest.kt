package github.ablandel.anotherkaomoji.repository

import github.ablandel.anotherkaomoji.entity.Tag
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

class TagRepositoryTest {

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Test
    @Transactional
    @Rollback
    fun `test findWithLimitAndOffset`() {
        tagRepository.save(Tag(label = "label1"))
        tagRepository.save(Tag(label = "label2"))
        tagRepository.save(Tag(label = "label3"))
        tagRepository.save(Tag(label = "label4"))
        val tags = tagRepository.findWithLimitAndOffset(1, 2)
        assertTrue(tags.size == 2)
        assertTrue(tags.all { tag -> tag.id != null })
        assertTrue(tags.all { tag -> tag.createdAt != null })
        assertTrue(tags.all { tag -> tag.updatedAt != null })
        assertTrue(tags.map { tag: Tag -> tag.label }.toList().containsAll(listOf("label2", "label3")))

        val tagsWithCursor = tagRepository.findWithLimitAndCursor(1L, 2)
        assertTrue(tagsWithCursor.size == 2)
        assertTrue(tagsWithCursor.all { tag -> tag.id != null && tag.id!! > 1L })
        assertTrue(tagsWithCursor.all { tag -> tag.createdAt != null })
        assertTrue(tagsWithCursor.all { tag -> tag.updatedAt != null })
    }

    @Test
    @Transactional
    @Rollback
    fun `test findByLabelIgnoreCase`() {
        tagRepository.save(Tag(label = "label1"))
        val tag = tagRepository.findByLabelIgnoreCase("label1")
        assertTrue(tag.isPresent)
        assertTrue(tag.get().label == "label1")
        val tagOtherCase = tagRepository.findByLabelIgnoreCase("Label1")
        assertTrue(tagOtherCase.isPresent)
        assertTrue(tagOtherCase.get().id != null)
        assertTrue(tagOtherCase.get().createdAt != null)
        assertTrue(tagOtherCase.get().updatedAt != null)
        assertTrue(tagOtherCase.get().label == "label1")
    }

    @Test
    @Transactional
    @Rollback
    fun `test findByLabelInIgnoreCase`() {
        tagRepository.save(Tag(label = "label1"))
        tagRepository.save(Tag(label = "label2"))
        tagRepository.save(Tag(label = "label3"))
        tagRepository.save(Tag(label = "label4"))
        val tags = tagRepository.findByLabelInIgnoreCase(listOf("label2", "Label3", "unknown"))
        assertTrue(tags.size == 2)
        assertTrue(tags.all { tag -> tag.id != null })
        assertTrue(tags.all { tag -> tag.createdAt != null })
        assertTrue(tags.all { tag -> tag.updatedAt != null })
        assertTrue(tags.map { tag: Tag -> tag.label }.toList().containsAll(listOf("label2", "label3")))
    }
}