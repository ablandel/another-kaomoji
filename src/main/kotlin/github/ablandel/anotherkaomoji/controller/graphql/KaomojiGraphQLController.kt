package github.ablandel.anotherkaomoji.controller.graphql

import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataDto
import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataPaginationDto
import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.KaomojiDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_CURSOR_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_LIMIT_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_OFFSET_QUERY
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.KaomojiService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class KaomojiGraphQLController(val kaomojiService: KaomojiService) {

    @QueryMapping
    fun findKaomojisWithPagination(
        @Argument(name = REQUEST_PAGINATION_CURSOR_QUERY) cursor: Long?,
        @Argument(name = REQUEST_PAGINATION_OFFSET_QUERY) offset: Int?,
        @Argument(name = REQUEST_PAGINATION_LIMIT_QUERY) limit: Int?
    ): GraphQLPaginatedDataDto<KaomojiDto> {
        val paginationDto = PaginationDto(offset = offset, limit = limit, cursor = cursor)
        val kaomojisDto = kaomojiService.findWithPagination(paginationDto)
        val totalCount = kaomojiService.count()
        val newCursor = if (kaomojisDto.isNotEmpty()) kaomojisDto[kaomojisDto.size - 1].id else null
        val graphQLPaginatedDataPaginationDto = if (cursor != null) {
            GraphQLPaginatedDataPaginationDto(
                limit = paginationDto.limit,
                cursor = newCursor
            )
        } else {
            GraphQLPaginatedDataPaginationDto(
                limit = paginationDto.limit,
                offset = paginationDto.offset,
                cursor = newCursor
            )
        }
        return GraphQLPaginatedDataDto(
            data = kaomojisDto,
            pagination = graphQLPaginatedDataPaginationDto,
            totalCount = totalCount
        )
    }

    @QueryMapping
    fun countKaomoji(): CountDto {
        return CountDto(kaomojiService.count())
    }

    @QueryMapping
    fun findKaomojiById(@Argument(name = "id") id: Long): KaomojiDto? {
        return kaomojiService.findById(id)
    }

    @MutationMapping
    fun deleteKaomojiById(@Argument(name = "id") id: Long): Boolean {
        kaomojiService.deleteById(id)
        return true // Not the prettiest piece of code, allow to announce the success to the client
    }

    @MutationMapping
    fun createKaomoji(
        @Argument(name = "key") key: String,
        @Argument(name = "emoticon") emoticon: String,
        @Argument(name = "tags") tags: List<TagDto>
    ): KaomojiDto {
        return kaomojiService.create(KaomojiDto(key = key, emoticon = emoticon, tags = tags))
    }

    @MutationMapping
    fun updateKaomoji(
        @Argument(name = "id") id: Long,
        @Argument(name = "key") key: String?,
        @Argument(name = "emoticon") emoticon: String?,
        @Argument(name = "tags") tags: List<TagDto>?
    ): KaomojiDto {
        return kaomojiService.update(id, KaomojiDto(id = id, key = key, emoticon = emoticon, tags = tags))
    }

    @MutationMapping
    fun replaceKaomoji(
        @Argument(name = "id") id: Long,
        @Argument(name = "key") key: String?,
        @Argument(name = "emoticon") emoticon: String?,
        @Argument(name = "tags") tags: List<TagDto>?
    ): KaomojiDto {
        return kaomojiService.replace(id, KaomojiDto(id = id, key = key, emoticon = emoticon, tags = tags))
    }
}