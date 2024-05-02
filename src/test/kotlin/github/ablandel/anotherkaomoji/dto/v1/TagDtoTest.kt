package github.ablandel.anotherkaomoji.dto.v1

import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

class TagDtoTest {

    @Test
    fun `fromDomain - throws InvalidParameterException when label is null`() {
        val tagDto = TagDto()

        assertThrows(InvalidParameterException::class.java) {
            TagDto.fromDomain(tagDto)
        }
    }

    @Test
    fun `fromDomain - throws InvalidParameterException when label is blank`() {
        val tagDto = TagDto(label = "")

        assertThrows(InvalidParameterException::class.java) {
            TagDto.fromDomain(tagDto)
        }
    }

    @Test
    fun `fromDomain - creates Tag object with valid parameters`() {
        val now = Instant.now()
        val tagDto = TagDto(createdAt = now, updatedAt = now, label = "label")

        val tag = TagDto.fromDomain(tagDto)

        assertNull(tag.createdAt)
        assertNull(tag.updatedAt)
        assertEquals(tagDto.label, tag.label)
    }

    @Test
    fun `replace - throws InvalidParameterException when label is null`() {
        val tag = Tag(label = "label")
        val tagDto = TagDto()

        assertThrows(InvalidParameterException::class.java) {
            TagDto.replace(tag, tagDto)
        }
    }

    @Test
    fun `replace - throws InvalidParameterException when label is blank`() {
        val tag = Tag(label = "label")
        val tagDto = TagDto(label = "")

        assertThrows(InvalidParameterException::class.java) {
            TagDto.replace(tag, tagDto)
        }
    }

    @Test
    fun `replace - creates Tag object with valid parameters`() {
        val now = Instant.now()
        val previousTag = Tag(
            id = 1L,
            label = "label",
        )
        val tagDto = TagDto(createdAt = now, updatedAt = now, label = "newLabel")

        val tag = TagDto.replace(previousTag, tagDto)

        assertEquals(1L, tag.id)
        assertNull(tag.createdAt)
        assertNull(tag.updatedAt)
        assertEquals(tagDto.label, tag.label)
    }

    @Test
    fun `update - update Tag object with valid parameters (no data)`() {
        val previousTag = Tag(
            id = 1L,
            label = "label",
        )
        val tagDto = TagDto()

        val tag = TagDto.update(previousTag, tagDto)

        assertEquals(1L, tag.id)
        assertEquals(previousTag.label, tag.label)
    }

    @Test
    fun `update - update Tag object with valid parameters`() {
        val now = Instant.now()
        val previousTag = Tag(
            id = 1L,
            label = "label",
        )
        val tagDto = TagDto(createdAt = now, updatedAt = now, label = "newLabel")

        val tag = TagDto.update(previousTag, tagDto)

        assertEquals(1L, tag.id)
        assertNull(tag.createdAt)
        assertNull(tag.updatedAt)
        assertEquals(tagDto.label, tag.label)
    }

    @Test
    fun `toDomain - create TagDto object with valid parameters`() {
        val now = Instant.now()
        val tag = Tag(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            label = "label",
        )

        val tagDto = TagDto.toDomain(tag)

        assertEquals(tag.id, tagDto.id)
        assertEquals(tag.createdAt, tagDto.createdAt)
        assertEquals(tag.updatedAt, tagDto.updatedAt)
        assertEquals(tag.label, tagDto.label)
    }
}