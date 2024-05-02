package github.ablandel.anotherkaomoji.controller.graphql

import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataDto
import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataPaginationDto
import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.KaomojiDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_LIMIT
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_OFFSET
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.KaomojiService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.Instant

class KaomojiGraphQLControllerTest {

    @Mock
    private lateinit var kaomojiService: KaomojiService

    @InjectMocks
    private lateinit var kaomojiGraphQLController: KaomojiGraphQLController

    private val now: Instant = Instant.now()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findKaomojisWithPagination returns list of KaomojiDto`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = kaomojiDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = paginationDto.offset,
                limit = paginationDto.limit,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result =
            kaomojiGraphQLController.findKaomojisWithPagination(null, paginationDto.offset, paginationDto.limit)
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `findKaomojisWithPagination returns list of KaomojiDto (with cursor)`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = kaomojiDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = null,
                limit = paginationDto.limit,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result = kaomojiGraphQLController.findKaomojisWithPagination(
            paginationDto.cursor,
            paginationDto.offset,
            paginationDto.limit
        )
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `findKaomojisWithPagination returns list of KaomojiDto (no pagination)`() {
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
        val graphQLPaginatedDataDto = GraphQLPaginatedDataDto(
            data = kaomojiDtos,
            pagination = GraphQLPaginatedDataPaginationDto(
                offset = PAGINATION_DEFAULT_OFFSET,
                limit = PAGINATION_DEFAULT_LIMIT,
                cursor = 2L
            ),
            totalCount = 42L
        )

        `when`(kaomojiService.findWithPagination(paginationDto))
            .thenReturn(kaomojiDtos)
        `when`(kaomojiService.count()).thenReturn(totalCount)

        val result = kaomojiGraphQLController.findKaomojisWithPagination(null, null, null)
        assertEquals(graphQLPaginatedDataDto, result)
    }

    @Test
    fun `countKaomoji returns the number of KaomojiDto`() {
        val totalCount = 42L
        `when`(kaomojiService.count()).thenReturn(totalCount)
        val result = kaomojiGraphQLController.countKaomoji()
        assertEquals(CountDto(totalCount), result)
    }

    @Test
    fun `findKaomojiById returns the KaomojiDto`() {
        val kaomojiDto = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.findById(1L)).thenReturn(kaomojiDto)
        val result = kaomojiGraphQLController.findKaomojiById(1L)
        assertEquals(kaomojiDto, result)
    }

    @Test
    fun `deleteKaomojiById deletes the KaomojiDto`() {
        kaomojiGraphQLController.deleteKaomojiById(1L)
        verify(kaomojiService).deleteById(1L)
    }

    @Test
    fun `createKaomoji creates the KaomojiDto`() {
        val kaomojiDto = KaomojiDto(
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
        val result = kaomojiGraphQLController.createKaomoji(
            key = kaomojiDto.key!!,
            emoticon = kaomojiDto.emoticon!!,
            tags = kaomojiDto.tags!!
        )
        assertEquals(kaomojiDtoWithId, result)
    }

    @Test
    fun `replaceKaomoji replaces the KaomojiDto`() {
        val kaomojiDtoWithId = KaomojiDto(
            id = 1L,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        val kaomojiDtoWithIdAndAt = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.replace(1L, kaomojiDtoWithId)).thenReturn(kaomojiDtoWithIdAndAt)
        val result = kaomojiGraphQLController.replaceKaomoji(
            id = 1L,
            key = kaomojiDtoWithId.key!!,
            emoticon = kaomojiDtoWithId.emoticon!!,
            tags = kaomojiDtoWithId.tags!!
        )
        assertEquals(kaomojiDtoWithIdAndAt, result)
    }

    @Test
    fun `update updates the KaomojiDto`() {
        val kaomojiDtoWithId = KaomojiDto(
            id = 1L,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        val kaomojiDtoWithIdAndAt = KaomojiDto(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key1",
            emoticon = "emoticon1",
            tags = listOf(TagDto(label = "label1"), TagDto(label = "label2"))
        )
        `when`(kaomojiService.update(1L, kaomojiDtoWithId)).thenReturn(kaomojiDtoWithIdAndAt)
        val result = kaomojiGraphQLController.updateKaomoji(
            id = 1L,
            key = kaomojiDtoWithId.key!!,
            emoticon = kaomojiDtoWithId.emoticon!!,
            tags = kaomojiDtoWithId.tags!!
        )
        assertEquals(kaomojiDtoWithIdAndAt, result)
    }
}