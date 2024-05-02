package github.ablandel.anotherkaomoji.service

import github.ablandel.anotherkaomoji.dto.v1.KaomojiDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.entity.Kaomoji
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.DataAlreadyExistException
import github.ablandel.anotherkaomoji.exception.DataNotFoundException
import github.ablandel.anotherkaomoji.exception.InconsistentParameterException
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import github.ablandel.anotherkaomoji.repository.KaomojiRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class KaomojiServiceTest {

    @Mock
    private lateinit var kaomojiRepository: KaomojiRepository

    @Mock
    private lateinit var tagService: TagService

    @InjectMocks
    private lateinit var kaomojiService: KaomojiService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto`() {
        val paginationDto = PaginationDto(0, 10)
        val kaomoji1 = Kaomoji(
            id = 1L,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomoji2 = Kaomoji(
            id = 2L,
            key = "key2",
            emoticon = "emoticon2",
            tags = listOf(
                Tag(id = 2L, label = "label2"),
                Tag(id = 3L, label = "label3")
            )
        )
        `when`(kaomojiRepository.findWithLimitAndOffset(0, 10)).thenReturn(listOf(kaomoji1, kaomoji2))

        val result = kaomojiService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertEquals(2, result.size)
        assertTrue(
            result.containsAll(
                listOf(
                    KaomojiDto(
                        id = 1L,
                        key = "key1",
                        emoticon = "emoticon1",
                        tags = listOf(TagDto(id = 1L, label = "label1"), TagDto(id = 2L, label = "label2"))
                    ),
                    KaomojiDto(
                        id = 2L,
                        key = "key2",
                        emoticon = "emoticon2",
                        tags = listOf(TagDto(id = 2L, label = "label2"), TagDto(id = 3L, label = "label3"))
                    )
                )
            )
        )
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto (empty)`() {
        val paginationDto = PaginationDto(0, 10)
        `when`(kaomojiRepository.findWithLimitAndOffset(0, 10)).thenReturn(listOf())

        val result = kaomojiService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto when cursor defined (empty)`() {
        val paginationDto = PaginationDto(0, 10, 42L)
        `when`(kaomojiRepository.findWithLimitAndCursor(42L, 10)).thenReturn(listOf())

        val result = kaomojiService.findWithPagination(paginationDto)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `count returns the number of Kaomoji`() {
        `when`(kaomojiRepository.count()).thenReturn(42)

        val result = kaomojiService.count()

        assertEquals(42, result)
    }

    @Test
    fun `findKaomojiById returns Kaomoji when found`() {
        val kaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomoji))

        val result = kaomojiService.findById(1L)
        val kaomojiDto = KaomojiDto(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(TagDto(id = 1L, label = "label1"), TagDto(id = 2L, label = "label2"))
        )
        assertEquals(kaomojiDto, result)
    }

    @Test
    fun `findKaomojiById throws DataNotFoundException when Kaomoji not found`() {
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(DataNotFoundException::class.java) {
            kaomojiService.findById(1L)
        }
    }

    @Test
    fun `deleteById delete Kaomoji when found`() {
        val kaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomoji))

        assertDoesNotThrow { kaomojiService.deleteById(1L) }
    }

    @Test
    fun `deleteById throws DataNotFoundException when Kaomoji not found`() {
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(DataNotFoundException::class.java) {
            kaomojiService.deleteById(1L)
        }
    }

    @Test
    fun `create throws InvalidParameterException when key is null`() {
        val kaomojiDto = KaomojiDto()

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create throws InvalidParameterException when key is blank`() {
        val kaomojiDto = KaomojiDto(key = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create throws InvalidParameterException when emoticon is null`() {
        val kaomojiDto = KaomojiDto(key = "key")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create throws InvalidParameterException when emoticon is blank`() {
        val kaomojiDto = KaomojiDto(key = "key", emoticon = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create throws InvalidParameterException when tags is null`() {
        val kaomojiDto = KaomojiDto(key = "key", emoticon = "emoticon")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create throws DataAlreadyExistException when Kaomoji found`() {
        val kaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(
            kaomojiRepository.findByKeyOrEmoticon(
                key = "key",
                emoticon = "emoticon"
            )
        ).thenReturn(listOf(kaomoji))

        val kaomojiDto = KaomojiDto(
            key = "key",
            emoticon = "emoticon",
            tags = listOf(TagDto(id = 1L, label = "label1"), TagDto(id = 2L, label = "label2"))
        )

        assertThrows(DataAlreadyExistException::class.java) {
            kaomojiService.create(kaomojiDto)
        }
    }

    @Test
    fun `create with valid parameters`() {
        `when`(
            kaomojiRepository.findByKeyOrEmoticon(
                key = "key",
                emoticon = "emoticon"
            )
        ).thenReturn(listOf())
        `when`(
            tagService.prepare(listOf("label1", "label2"))
        ).thenReturn(
            listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomoji = Kaomoji(
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomojiWithId = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.save(kaomoji)).thenReturn(kaomojiWithId)

        val kaomojiDto = KaomojiDto(
            key = "key",
            emoticon = "emoticon",
            tags = listOf(TagDto(id = 1L, label = "label1"), TagDto(id = 2L, label = "label2"))
        )

        val result = kaomojiService.create(kaomojiDto)

        assertEquals(kaomojiWithId.id, result.id)
        assertEquals(kaomojiDto.key, result.key)
        assertEquals(kaomojiDto.emoticon, result.emoticon)
        assertEquals(2, result.tags!!.size)
        assertTrue(result.tags!!.containsAll(kaomojiDto.tags!!))
    }

    @Test
    fun `replace throws InconsistentParameterException when id is inconsistent`() {
        val kaomojiDto = KaomojiDto(id = 42L)

        assertThrows(InconsistentParameterException::class.java) {
            kaomojiService.replace(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `replace throws InvalidParameterException when key is blank`() {
        val kaomojiDto = KaomojiDto(id = 1L, key = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.replace(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `replace throws InvalidParameterException when emoticon is blank`() {
        val kaomojiDto = KaomojiDto(id = 1L, key = "key", emoticon = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.replace(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `replace (tags defined)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label2", "label3"))
        ).thenReturn(
            listOf(
                Tag(id = 2L, label = "label2"),
                Tag(id = 3L, label = "label3")
            )
        )
        val kaomoji = Kaomoji(
            id = 1L,
            key = "newKey",
            emoticon = "newEmoticon",
            tags = listOf(
                Tag(id = 2L, label = "label2"),
                Tag(id = 3L, label = "label3")
            )
        )
        `when`(kaomojiRepository.save(kaomoji)).thenReturn(kaomoji)

        val kaomojiDto =
            KaomojiDto(
                id = 1L,
                key = "newKey",
                emoticon = "newEmoticon",
                tags = listOf(TagDto(id = 2L, label = "label2"), TagDto(id = 3L, label = "label3"))
            )

        val result = kaomojiService.replace(1L, kaomojiDto)

        assertEquals(kaomojiDto.id, result.id)
        assertEquals(kaomojiDto.key, result.key)
        assertEquals(kaomojiDto.emoticon, result.emoticon)
        assertEquals(2, result.tags!!.size)
        assertTrue(result.tags!!.containsAll(kaomojiDto.tags!!))
    }

    @Test
    fun `replace (key not defined)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label1", "label2"))
        ).thenReturn(
            listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )

        val kaomojiDto =
            KaomojiDto(id = 1L)

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.replace(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `replace (emoticon not defined)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label1", "label2"))
        ).thenReturn(
            listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )

        val kaomojiDto =
            KaomojiDto(id = 1L, key = "key")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.replace(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `replace (tag not defined)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label1", "label2"))
        ).thenReturn(
            listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomoji = Kaomoji(
            id = 1L,
            key = "newKey",
            emoticon = "newEmoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.save(kaomoji)).thenReturn(kaomoji)

        val kaomojiDto =
            KaomojiDto(id = 1L, key = "newKey", emoticon = "newEmoticon")

        val result = kaomojiService.replace(1L, kaomojiDto)

        assertEquals(kaomojiDto.id, result.id)
        assertEquals(kaomojiDto.key, result.key)
        assertEquals(kaomojiDto.emoticon, result.emoticon)
        assertEquals(2, result.tags!!.size)
        assertTrue(
            result.tags!!.containsAll(
                listOf(
                    TagDto(id = 1L, label = "label1"),
                    TagDto(id = 2L, label = "label2")
                )
            )
        )
    }

    @Test
    fun `update throws InconsistentParameterException when id is inconsistent`() {
        val kaomojiDto = KaomojiDto(id = 42L)

        assertThrows(InconsistentParameterException::class.java) {
            kaomojiService.update(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `update throws InvalidParameterException when key is blank`() {
        val kaomojiDto = KaomojiDto(id = 1L, key = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.update(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `update throws InvalidParameterException when emoticon is blank`() {
        val kaomojiDto = KaomojiDto(id = 1L, key = "key", emoticon = "")

        assertThrows(InvalidParameterException::class.java) {
            kaomojiService.update(id = 1L, kaomojiDto)
        }
    }

    @Test
    fun `update (all defined)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label2", "label3"))
        ).thenReturn(
            listOf(
                Tag(id = 2L, label = "label2"),
                Tag(id = 3L, label = "label3")
            )
        )
        val kaomoji = Kaomoji(
            id = 1L,
            key = "newKey",
            emoticon = "newEmoticon",
            tags = listOf(
                Tag(id = 2L, label = "label2"),
                Tag(id = 3L, label = "label3")
            )
        )
        `when`(kaomojiRepository.save(kaomoji)).thenReturn(kaomoji)

        val kaomojiDto =
            KaomojiDto(
                id = 1L,
                key = "newKey",
                emoticon = "newEmoticon",
                tags = listOf(TagDto(id = 2L, label = "label2"), TagDto(id = 3L, label = "label3"))
            )

        val result = kaomojiService.update(1L, kaomojiDto)

        assertEquals(kaomojiDto.id, result.id)
        assertEquals(kaomojiDto.key, result.key)
        assertEquals(kaomojiDto.emoticon, result.emoticon)
        assertEquals(2, result.tags!!.size)
        assertTrue(
            result.tags!!.containsAll(
                listOf(
                    TagDto(id = 2L, label = "label2"),
                    TagDto(id = 3L, label = "label3")
                )
            )
        )
    }

    @Test
    fun `update (not modification)`() {
        val kaomojiDb = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.findById(1L)).thenReturn(Optional.of(kaomojiDb))
        `when`(
            tagService.prepare(listOf("label1", "label2"))
        ).thenReturn(
            listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        `when`(kaomojiRepository.save(kaomoji)).thenReturn(kaomoji)

        val kaomojiDto =
            KaomojiDto(id = 1L)

        val result = kaomojiService.update(1L, kaomojiDto)

        assertEquals(kaomojiDto.id, result.id)
        assertEquals(kaomoji.key, result.key)
        assertEquals(kaomoji.emoticon, result.emoticon)
        assertEquals(2, result.tags!!.size)
        assertTrue(
            result.tags!!.containsAll(
                listOf(
                    TagDto(id = 1L, label = "label1"),
                    TagDto(id = 2L, label = "label2")
                )
            )
        )
    }
}