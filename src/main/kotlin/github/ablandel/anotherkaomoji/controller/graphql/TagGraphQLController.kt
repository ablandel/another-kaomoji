package github.ablandel.anotherkaomoji.controller.graphql

import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataDto
import github.ablandel.anotherkaomoji.dto.graphql.GraphQLPaginatedDataPaginationDto
import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_CURSOR_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_LIMIT_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_OFFSET_QUERY
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.TagService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class TagGraphQLController(val tagService: TagService) {

    @QueryMapping
    fun findTagsWithPagination(
        @Argument(name = REQUEST_PAGINATION_CURSOR_QUERY) cursor: Long?,
        @Argument(name = REQUEST_PAGINATION_OFFSET_QUERY) offset: Int?,
        @Argument(name = REQUEST_PAGINATION_LIMIT_QUERY) limit: Int?
    ): GraphQLPaginatedDataDto<TagDto> {
        val paginationDto = PaginationDto(offset = offset, limit = limit, cursor = cursor)
        val tagsDto = tagService.findWithPagination(paginationDto)
        val totalCount = tagService.count()
        val newCursor = if (tagsDto.isNotEmpty()) tagsDto[tagsDto.size - 1].id else null
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
            data = tagsDto,
            pagination = graphQLPaginatedDataPaginationDto,
            totalCount = totalCount
        )
    }

    @QueryMapping
    fun countTag(): CountDto {
        return CountDto(tagService.count())
    }

    @QueryMapping
    fun findTagById(@Argument(name = "id") id: Long): TagDto? {
        return tagService.findById(id)
    }

    @MutationMapping
    fun deleteTagById(@Argument(name = "id") id: Long): Boolean {
        tagService.deleteById(id)
        return true
    }

    @MutationMapping
    fun createTag(@Argument(name = "label") label: String): TagDto {
        return tagService.create(TagDto(label = label))
    }

    @MutationMapping
    fun updateTag(@Argument(name = "id") id: Long, @Argument(name = "label") label: String): TagDto {
        return tagService.update(id, TagDto(id = id, label = label))
    }

    @MutationMapping
    fun replaceTag(@Argument(name = "id") id: Long, @Argument(name = "label") label: String): TagDto {
        return tagService.replace(id, TagDto(id = id, label = label))
    }
}