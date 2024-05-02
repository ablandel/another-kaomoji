package github.ablandel.anotherkaomoji.controller.v1

import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_LIMIT
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_OFFSET
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.TagService
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.Instant

class TagControllerTest {

    @Mock
    private lateinit var tagService: TagService

    @InjectMocks
    private lateinit var tagController: TagController

    private val now: Instant = Instant.now()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findWithPagination returns list of TagDto`() {
        val tagDtos = listOf(
            TagDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                label = "label1"
            ),
            TagDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                label = "label2"
            )
        )
        val paginationDto = PaginationDto(10, 10)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagController.findWithPagination(null, paginationDto.offset, paginationDto.limit, responseMock)
        assertEquals(tagDtos, result)
    }

    @Test
    fun `findWithPagination returns list of TagDto (with cursor)`() {
        val tagDtos = listOf(
            TagDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                label = "label1"
            ),
            TagDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                label = "label2"
            )
        )
        val paginationDto = PaginationDto(10, 10, 666L)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagController.findWithPagination(
            paginationDto.cursor,
            paginationDto.offset,
            paginationDto.limit,
            responseMock
        )
        assertEquals(tagDtos, result)
    }

    @Test
    fun `findWithPagination returns list of TagDto (no pagination)`() {
        val tagDtos = listOf(
            TagDto(
                id = 1L,
                createdAt = now,
                updatedAt = now,
                label = "label1"
            ),
            TagDto(
                id = 2L,
                createdAt = now,
                updatedAt = now,
                label = "label2"
            )
        )
        val paginationDto = PaginationDto(PAGINATION_DEFAULT_OFFSET, PAGINATION_DEFAULT_LIMIT)
        val totalCount = 42L
        val responseMock = mock(HttpServletResponse::class.java)

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagController.findWithPagination(null, null, null, responseMock)
        assertEquals(tagDtos, result)
    }

    @Test
    fun `count returns the number of TagDto`() {
        val totalCount = 42L
        `when`(tagService.count()).thenReturn(totalCount)
        val result = tagController.count()
        assertEquals(CountDto(totalCount), result)
    }

    @Test
    fun `findById returns the TagDto`() {
        val tagDto = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.findById(1L)).thenReturn(tagDto)
        val result = tagController.findById(1L)
        assertEquals(tagDto, result)
    }

    @Test
    fun `deleteById deletes the TagDto`() {
        tagController.deleteById(1L)
        verify(tagService).deleteById(1L)
    }

    @Test
    fun `create creates the TagDto`() {
        val tagDto = TagDto(
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        val tagDtoWithId = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.create(tagDto)).thenReturn(tagDtoWithId)
        val result = tagController.create(tagDto)
        assertEquals(tagDtoWithId, result)
    }

    @Test
    fun `replace replaces the TagDto`() {
        val tagDtoWithId = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.replace(1L, tagDtoWithId)).thenReturn(tagDtoWithId)
        val result = tagController.replace(1L, tagDtoWithId)
        assertEquals(tagDtoWithId, result)
    }

    @Test
    fun `update updates the TagDto`() {
        val tagDto = TagDto(
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        val tagDtoWithId = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.update(1L, tagDto)).thenReturn(tagDtoWithId)
        val result = tagController.update(1L, tagDto)
        assertEquals(tagDtoWithId, result)
    }
}