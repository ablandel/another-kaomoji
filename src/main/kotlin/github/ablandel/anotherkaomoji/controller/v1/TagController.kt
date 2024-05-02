package github.ablandel.anotherkaomoji.controller.v1

import github.ablandel.anotherkaomoji.dto.Validation
import github.ablandel.anotherkaomoji.dto.v1.CountDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.PAGINATION_DEFAULT_LIMIT
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_CURSOR_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_LIMIT_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.REQUEST_PAGINATION_OFFSET_QUERY
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.RESPONSE_PAGINATION_COUNT_HEADER
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.RESPONSE_PAGINATION_CURSOR_HEADER
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.RESPONSE_PAGINATION_DEFAULT_LIMIT_HEADER
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.RESPONSE_PAGINATION_OFFSET_HEADER
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto.Companion.RESPONSE_PAGINATION_TOTAL_COUNT_HEADER
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.service.TagService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/v1/tags")
class TagController(val tagService: TagService) {

    @GetMapping
    fun findWithPagination(
        @RequestParam(required = false, name = REQUEST_PAGINATION_CURSOR_QUERY) cursor: Long?,
        @RequestParam(required = false, name = REQUEST_PAGINATION_OFFSET_QUERY) offset: Int?,
        @RequestParam(required = false, name = REQUEST_PAGINATION_LIMIT_QUERY) limit: Int?,
        response: HttpServletResponse,
    ): List<TagDto> {
        val paginationDto = PaginationDto(offset = offset, limit = limit, cursor = cursor)
        val tagsDto = tagService.findWithPagination(paginationDto)
        val totalCount = tagService.count()
        if (offset != null) {
            response.setHeader(
                RESPONSE_PAGINATION_OFFSET_HEADER,
                if (paginationDto.offset < totalCount) paginationDto.offset.toString() else totalCount.toString()
            )
        }
        if (tagsDto.isNotEmpty()) {
            response.setHeader(
                RESPONSE_PAGINATION_CURSOR_HEADER,
                tagsDto[tagsDto.size - 1].id.toString()
            )
        }
        response.setHeader(RESPONSE_PAGINATION_DEFAULT_LIMIT_HEADER, PAGINATION_DEFAULT_LIMIT.toString())
        response.setHeader(RESPONSE_PAGINATION_COUNT_HEADER, tagsDto.size.toString())
        response.setHeader(RESPONSE_PAGINATION_TOTAL_COUNT_HEADER, totalCount.toString())
        return tagsDto
    }

    @GetMapping("/count")
    fun count(): CountDto {
        return CountDto(tagService.count())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): TagDto {
        return tagService.findById(id)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) {
        tagService.deleteById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Validated(Validation.Create::class) @RequestBody tagDto: TagDto): TagDto {
        return tagService.create(tagDto)
    }

    @PutMapping("/{id}")
    fun replace(
        @PathVariable id: Long,
        @Validated(Validation.Replace::class)
        @RequestBody tagDto: TagDto
    ): TagDto {
        return tagService.replace(id, tagDto)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Validated(Validation.Update::class)
        @RequestBody tagDto: TagDto
    ): TagDto {
        return tagService.update(id, tagDto)
    }
}