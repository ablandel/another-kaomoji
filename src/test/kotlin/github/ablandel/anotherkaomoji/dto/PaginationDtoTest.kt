package github.ablandel.anotherkaomoji.dto

import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PaginationDtoTest {

    @Test
    fun `constructor with default values`() {
        val paginationDto = PaginationDto(offset = null, limit = null)
        assertEquals(0, paginationDto.offset)
        assertEquals(20, paginationDto.limit)
    }
}

