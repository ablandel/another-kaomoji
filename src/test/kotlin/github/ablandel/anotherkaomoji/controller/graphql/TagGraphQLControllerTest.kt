package github.ablandel.anotherkaomoji.controller.graphql

import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataDto
import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataPaginationDto
import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_LIMIT
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_OFFSET
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.TagService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.Instant

class TagGraphQLControllerTest {

    @Mock
    private lateinit var tagService: TagService

    @InjectMocks
    private lateinit var tagGraphQLController: TagGraphQLController

    private val now: Instant = Instant.now()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findTagsWithPagination returns list of TagDto`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = tagDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = paginationDto.offset,
                limit = paginationDto.limit,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagGraphQLController.findTagsWithPagination(null, paginationDto.offset, paginationDto.limit)
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `findTagsWithPagination returns list of TagDto (with cursor)`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = tagDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = null,
                limit = paginationDto.limit,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagGraphQLController.findTagsWithPagination(
            paginationDto.cursor,
            paginationDto.offset,
            paginationDto.limit
        )
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `findTagsWithPagination returns list of TagDto (no pagination)`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = tagDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = PAGINATION_DEFAULT_OFFSET,
                limit = PAGINATION_DEFAULT_LIMIT,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(tagService.findWithPagination(paginationDto))
            .thenReturn(tagDtos)
        `when`(tagService.count()).thenReturn(totalCount)

        val result = tagGraphQLController.findTagsWithPagination(null, null, null)
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `countTag returns the number of TagDto`() {
        val totalCount = 42L
        `when`(tagService.count()).thenReturn(totalCount)
        val result = tagGraphQLController.countTag()
        assertEquals(CountDto(totalCount), result)
    }

    @Test
    fun `findTagById returns the TagDto`() {
        val tagDto = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.findById(1L)).thenReturn(tagDto)
        val result = tagGraphQLController.findTagById(1L)
        assertEquals(tagDto, result)
    }

    @Test
    fun `deleteTagById deletes the TagDto`() {
        tagGraphQLController.deleteTagById(1L)
        verify(tagService).deleteById(1L)
    }

    @Test
    fun `createTag creates the TagDto`() {
        val tagDto = TagDto(
            label = "label1"
        )
        val tagDtoWithId = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.create(tagDto)).thenReturn(tagDtoWithId)
        val result = tagGraphQLController.createTag(label = tagDto.label!!)
        assertEquals(tagDtoWithId, result)
    }

    @Test
    fun `replaceTag replaces the TagDto`() {
        val tagDtoWithId = TagDto(
            id = 1L,
            label = "label1"
        )
        val tagDtoWithIdAndAt = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.replace(1L, tagDtoWithId)).thenReturn(tagDtoWithIdAndAt)
        val result = tagGraphQLController.replaceTag(1L, label = tagDtoWithId.label!!)
        assertEquals(tagDtoWithIdAndAt, result)
    }

    @Test
    fun `updateTag updates the TagDto`() {
        val tagDtoWithId = TagDto(
            id = 1L,
            label = "label1"
        )
        val tagDtoWithIdAndAt = TagDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label1"
        )
        `when`(tagService.update(1L, tagDtoWithId)).thenReturn(tagDtoWithIdAndAt)
        val result = tagGraphQLController.updateTag(1L, label = tagDtoWithId.label!!)
        assertEquals(tagDtoWithIdAndAt, result)
    }
}