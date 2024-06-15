package github.ablandel.anotherkaomoji.service

import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.DataAlreadyExistException
import github.ablandel.anotherkaomoji.exception.DataNotFoundException
import github.ablandel.anotherkaomoji.exception.InconsistentParameterException
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import github.ablandel.anotherkaomoji.repository.TagRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class TagServiceTest {

    @Mock
    private lateinit var tagRepository: TagRepository

    @InjectMocks
    private lateinit var tagService: TagService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findWithPagination returns list of TagDto`() {
        val paginationDto = PaginationDto(0, 10)
        val tag1 = Tag(
            id = 1L,
            label = "label1"
        )
        val tag2 = Tag(
            id = 2L,
            label = "label2"
        )
        `when`(tagRepository.findWithLimitAndOffset(0, 10)).thenReturn(listOf(tag1, tag2))

        val result = tagService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertEquals(2, result.size)
        assertTrue(
            result.containsAll(
                listOf(
                    TagDto(
                        id = 1L,
                        label = "label1"
                    ),
                    TagDto(
                        id = 2L,
                        label = "label2"
                    )
                )
            )
        )
    }

    @Test
    fun `findWithPagination returns list of TagDto (empty)`() {
        val paginationDto = PaginationDto(0, 10)
        `when`(tagRepository.findWithLimitAndOffset(0, 10)).thenReturn(listOf())

        val result = tagService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findWithPagination returns list of TagDto when cursor defined (empty)`() {
        val paginationDto = PaginationDto(0, 10, 42L)
        `when`(tagRepository.findWithLimitAndCursor(42L, 10)).thenReturn(listOf())

        val result = tagService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `count returns the number of Tag`() {
        `when`(tagRepository.count()).thenReturn(42)

        val result = tagService.count()

        assertEquals(42, result)
    }

    @Test
    fun `findTagById returns Tag when found`() {
        val tag = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tag))

        val result = tagService.findById(1L)
        val tagDto = TagDto(
            id = 1L,
            label = "label"
        )
        assertEquals(tagDto, result)
    }

    @Test
    fun `findTagById throws DataNotFoundException when Tag not found`() {
        `when`(tagRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(DataNotFoundException::class.java) {
            tagService.findById(1L)
        }
    }

    @Test
    fun `deleteById delete Tag when found`() {
        val tag = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tag))

        assertDoesNotThrow { tagService.deleteById(1L) }
    }

    @Test
    fun `deleteById throws DataNotFoundException when Tag not found`() {
        `when`(tagRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(DataNotFoundException::class.java) {
            tagService.deleteById(1L)
        }
    }

    @Test
    fun `create throws InvalidParameterException when label is null`() {
        val tagDto = TagDto()

        assertThrows(InvalidParameterException::class.java) {
            tagService.create(tagDto)
        }
    }

    @Test
    fun `create throws InvalidParameterException when label is blank`() {
        val tagDto = TagDto(label = "")

        assertThrows(InvalidParameterException::class.java) {
            tagService.create(tagDto)
        }
    }

    @Test
    fun `create throws DataAlreadyExistException when Kaomoji found`() {
        val tag = Tag(
            id = 1L,
            label = "label"
        )
        `when`(
            tagRepository.findByLabelIgnoreCase(
                label = "label"
            )
        ).thenReturn(Optional.of(tag))

        val tagDto = TagDto(label = "label")

        assertThrows(DataAlreadyExistException::class.java) {
            tagService.create(tagDto)
        }
    }

    @Test
    fun `create with valid parameters`() {
        `when`(
            tagRepository.findByLabelIgnoreCase(
                label = "label"
            )
        ).thenReturn(Optional.empty())
        val tag = Tag(
            label = "label"
        )
        val tagWithId = Tag(
            id = 1L,
            label = "label"
        )
        `when`(
            tagRepository.save(tag)
        ).thenReturn(tagWithId)

        val tagDto = TagDto(label = "label")

        val result = tagService.create(tagDto)

        assertEquals(tagWithId.id, result.id)
        assertEquals(tagDto.label, result.label)
    }

    @Test
    fun `prepare with only existing tags`() {
        val existingTags = listOf(Tag(id = 1L, label = "label1"), Tag(id = 2L, label = "label2"))
        val inputTags = listOf("label1", "label2")

        `when`(tagRepository.findByLabelInIgnoreCase(inputTags)).thenReturn(existingTags)

        val result = tagService.prepare(inputTags)
        assertEquals(existingTags, result)
    }

    @Test
    fun `prepare with new and existing tags`() {
        val existingTags = listOf(Tag(id = 1L, label = "label1"), Tag(id = 2L, label = "label2"))
        val newTags = listOf(Tag(id = 3L, label = "label3"), Tag(id = 4L, label = "label4"))
        val inputTags = listOf("label1", "label2", "label3", "label4")

        `when`(tagRepository.findByLabelInIgnoreCase(inputTags)).thenReturn(existingTags)
        `when`(tagRepository.saveAll(listOf(Tag(label = "label3"), Tag(label = "label4")))).thenReturn(newTags)

        val result = tagService.prepare(inputTags)
        assertEquals(existingTags + newTags, result)
    }

    @Test
    fun `prepare with new and existing tags - case-insensitive match`() {
        val existingTags = listOf(Tag(id = 1L, label = "label1"), Tag(id = 2L, label = "label2"))
        val newTag = Tag(id = 3L, label = "lAbel3")
        val inputTags = listOf("label1", "lAbel2", "lAbel3")

        `when`(tagRepository.findByLabelInIgnoreCase(inputTags)).thenReturn(existingTags)
        `when`(tagRepository.saveAll(listOf(Tag(label = "lAbel3")))).thenReturn(listOf(newTag))

        val result = tagService.prepare(inputTags)
        assertEquals(existingTags + newTag, result)
    }

    @Test
    fun `prepare with duplicate tags`() {
        val existingTags = listOf(Tag("tag1", 1L), Tag("tag2", 2L))
        val inputTags = listOf("Tag1", "tag2", "tag1")

        `when`(tagRepository.findByLabelInIgnoreCase(inputTags)).thenReturn(existingTags)

        val result = tagService.prepare(inputTags)
        assertEquals(existingTags, result)
    }

    @Test
    fun `replace throws InconsistentParameterException when id is inconsistent`() {
        val tagDto = TagDto(id = 42L)

        assertThrows(InconsistentParameterException::class.java) {
            tagService.replace(id = 1L, tagDto)
        }
    }

    @Test
    fun `replace throws InvalidParameterException when label is blank`() {
        val tagDto = TagDto(id = 1L, label = "")

        assertThrows(InvalidParameterException::class.java) {
            tagService.replace(id = 1L, tagDto)
        }
    }

    @Test
    fun `replace (label not defined)`() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))

        val tagDto =
            TagDto(id = 1L)

        assertThrows(InvalidParameterException::class.java) {
            tagService.replace(id = tagDb.id!!, tagDto)
        }
    }

    @Test
    fun `replace throws DataAlreadyExistException when label is already use by another tag`() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))

        val tagDto =
            TagDto(id = 1L, label = "label")

        val tagWithLabelAlreadyUsedDb = Tag(
            id = 2L,
            label = "label"
        )
        `when`(tagRepository.findByLabelIgnoreCase("label")).thenReturn(Optional.of(tagWithLabelAlreadyUsedDb))

        assertThrows(DataAlreadyExistException::class.java) {
            tagService.replace(id = 1L, tagDto)
        }
    }

    @Test
    fun replace() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))
        `when`(tagRepository.findByLabelIgnoreCase("label")).thenReturn(Optional.of(tagDb))

        val tag = Tag(
            id = 1L,
            label = "newLabel",
        )
        `when`(tagRepository.save(tag)).thenReturn(tag)

        val tagDto =
            TagDto(id = 1L, label = "newLabel")

        val result = tagService.replace(1L, tagDto)

        assertEquals(tagDto.id, result.id)
        assertEquals(tagDto.label, result.label)
    }

    @Test
    fun `update throws InconsistentParameterException when id is inconsistent`() {
        val tagDto = TagDto(id = 42L)

        assertThrows(InconsistentParameterException::class.java) {
            tagService.update(id = 1L, tagDto)
        }
    }

    @Test
    fun `update throws InvalidParameterException when label is blank`() {
        val tagDto = TagDto(id = 1L, label = "")

        assertThrows(InvalidParameterException::class.java) {
            tagService.update(id = 1L, tagDto)
        }
    }

    @Test
    fun `update (all defined)`() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))
        `when`(tagRepository.findByLabelIgnoreCase("label")).thenReturn(Optional.of(tagDb))

        val tag = Tag(
            id = 1L,
            label = "newLabel",
        )
        `when`(tagRepository.save(tag)).thenReturn(tag)

        val tagDto =
            TagDto(id = 1L, label = "newLabel")

        val result = tagService.update(1L, tagDto)

        assertEquals(tagDto.id, result.id)
        assertEquals(tagDto.label, result.label)
    }

    @Test
    fun `update (no modification)`() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))

        val tag = Tag(
            id = 1L,
            label = "label",
        )
        `when`(tagRepository.save(tag)).thenReturn(tag)

        val tagDto =
            TagDto(id = 1L)

        val result = tagService.update(1L, tagDto)

        assertEquals(tagDto.id, result.id)
        assertEquals(tag.label, result.label)
    }

    @Test
    fun `update throws DataAlreadyExistException when label is already use by another tag`() {
        val tagDb = Tag(
            id = 1L,
            label = "label"
        )
        `when`(tagRepository.findById(1L)).thenReturn(Optional.of(tagDb))

        val tagDto =
            TagDto(id = 1L, label = "label")

        val tagWithLabelAlreadyUsedDb = Tag(
            id = 2L,
            label = "label"
        )
        `when`(tagRepository.findByLabelIgnoreCase("label")).thenReturn(Optional.of(tagWithLabelAlreadyUsedDb))

        assertThrows(DataAlreadyExistException::class.java) {
            tagService.update(id = 1L, tagDto)
        }
    }
}