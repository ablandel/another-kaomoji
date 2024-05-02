package github.ablandel.anotherkaomoji.controller.v1

import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.KaomojiDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_LIMIT
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_OFFSET
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.KaomojiService
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.Instant

class KaomojiControllerTest {

    @Mock
    private lateinit var kaomojiService: KaomojiService

    @InjectMocks
    private lateinit var kaomojiController: KaomojiController

    private val now: Instant = Instant.now()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto`() {
        val kaomojiDtos = listOf(
            KaomojiDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
            ),
            KaomojiDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                key = "key2",
                emoticon = "emoticon2",
                tags = listOf(TagDto(label = "label2"), TagDto(label = "label3"))
            )
        )
        val paginationDto = PaginationDto(10, 10)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result = kaomojiController.findWithPagination(null, paginationDto.offset, paginationDto.limit, responseMock)
        assertEquals(kaomojiDtos, result)
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto (with cursor)`() {
        val kaomojiDtos = listOf(
            KaomojiDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
            ),
            KaomojiDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                key = "key2",
                emoticon = "emoticon2",
                tags = listOf(TagDto(label = "label2"), TagDto(label = "label3"))
            )
        )
        val paginationDto = PaginationDto(10, 10, 666L)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result = kaomojiController.findWithPagination(
            paginationDto.cursor,
            paginationDto.offset,
            paginationDto.limit,
            responseMock
        )
        assertEquals(kaomojiDtos, result)
    }

    @Test
    fun `findWithPagination returns list of KaomojiDto (no pagination)`() {
        val kaomojiDtos = listOf(
            KaomojiDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                key = "key1",
                emoticon = "emoticon1",
                tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
            ),
            KaomojiDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                key = "key2",
                emoticon = "emoticon2",
                tags = listOf(TagDto(label = "label2"), TagDto(label = "label3"))
            )
        )
        val paginationDto = PaginationDto(PAGINATION_DEFAULT_OFFSET, PAGINATION_DEFAULT_LIMIT)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result = kaomojiController.findWithPagination(null, null, null, responseMock)
        assertEquals(kaomojiDtos, result)
    }

    @Test
    fun `count returns the number of KaomojiDto`() {
        val totalCount = 42L
        `when`(kaomojiService.count()).thenReturn(totalCount)
        val result = kaomojiController.count()
        assertEquals(CountDto(totalCount), result)
    }

    @Test
    fun `findById returns the KaomojiDto`() {
        val kaomojiDto = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.findById(1L)).thenReturn(kaomojiDto)
        val result = kaomojiController.findById(1L)
        assertEquals(kaomojiDto, result)
    }

    @Test
    fun `deleteById deletes the KaomojiDto`() {
        kaomojiController.deleteById(1L)
        verify(kaomojiService).deleteById(1L)
    }

    @Test
    fun `create creates the KaomojiDto`() {
        val kaomojiDto = KaomojiDto(
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        val kaomojiDtoWithId = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.create(kaomojiDto)).thenReturn(kaomojiDtoWithId)
        val result = kaomojiController.create(kaomojiDto)
        assertEquals(kaomojiDtoWithId, result)
    }

    @Test
    fun `replace replaces the KaomojiDto`() {
        val kaomojiDtoWithId = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.replace(1L, kaomojiDtoWithId)).thenReturn(kaomojiDtoWithId)
        val result = kaomojiController.replace(1L, kaomojiDtoWithId)
        assertEquals(kaomojiDtoWithId, result)
    }

    @Test
    fun `update updates the KaomojiDto`() {
        val kaomojiDto = KaomojiDto(
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        val kaomojiDtoWithId = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.update(1L, kaomojiDto)).thenReturn(kaomojiDtoWithId)
        val result = kaomojiController.update(1L, kaomojiDto)
        assertEquals(kaomojiDtoWithId, result)
    }
}