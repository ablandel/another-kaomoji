package github.ablandel.anotherkaomoji.dto.v1

import github.ablandel.anotherkaomoji.entity.Kaomoji
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

class KaomojiDtoTest {

    @Test
    fun `fromDomain - throws InvalidParameterException when key is null`() {
        val kaomojiDto = KaomojiDto()
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.fromDomain(kaomojiDto, tags)
        }
    }

    @Test
    fun `fromDomain - throws InvalidParameterException when key is blank`() {
        val kaomojiDto = KaomojiDto(key = "")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.fromDomain(kaomojiDto, tags)
        }
    }

    @Test
    fun `fromDomain - throws InvalidParameterException when emoticon is null`() {
        val kaomojiDto = KaomojiDto(key = "key")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.fromDomain(kaomojiDto, tags)
        }
    }

    @Test
    fun `fromDomain - throws InvalidParameterException when emoticon is blank`() {
        val kaomojiDto = KaomojiDto(key = "key", emoticon = "")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.fromDomain(kaomojiDto, tags)
        }
    }

    @Test
    fun `fromDomain - creates Kaomoji object with valid parameters`() {
        val now = Instant.now()
        val kaomojiDto = KaomojiDto(createdAt = now, updatedAt = now, key = "key", emoticon = "emoticon")
        val tags = listOf(Tag(label = "label1"), Tag(label = "label2"))

        val kaomoji = KaomojiDto.fromDomain(kaomojiDto, tags)

        assertNull(kaomoji.createdAt)
        assertNull(kaomoji.updatedAt)
        assertEquals(kaomojiDto.key, kaomoji.key)
        assertEquals(kaomojiDto.emoticon, kaomoji.emoticon)
        assertEquals(2, kaomoji.tags.size)
        assertTrue(kaomoji.tags.containsAll(tags))
    }

    @Test
    fun `replace - throws InvalidParameterException when key is null`() {
        val kaomoji = Kaomoji(key = "key", emoticon = "emoticon", tags = listOf())
        val kaomojiDto = KaomojiDto()
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.replace(kaomoji, kaomojiDto, tags)
        }
    }

    @Test
    fun `replace - throws InvalidParameterException when key is blank`() {
        val kaomoji = Kaomoji(key = "key", emoticon = "emoticon", tags = listOf())
        val kaomojiDto = KaomojiDto(key = "")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.replace(kaomoji, kaomojiDto, tags)
        }
    }

    @Test
    fun `replace - throws InvalidParameterException when emoticon is null`() {
        val kaomoji = Kaomoji(key = "key", emoticon = "emoticon", tags = listOf())
        val kaomojiDto = KaomojiDto(key = "key")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.replace(kaomoji, kaomojiDto, tags)
        }
    }

    @Test
    fun `replace - throws InvalidParameterException when emoticon is blank`() {
        val kaomoji = Kaomoji(key = "key", emoticon = "emoticon", tags = listOf())
        val kaomojiDto = KaomojiDto(key = "key", emoticon = "")
        val tags = listOf<Tag>()

        assertThrows(InvalidParameterException::class.java) {
            KaomojiDto.replace(kaomoji, kaomojiDto, tags)
        }
    }

    @Test
    fun `replace - creates Kaomoji object with valid parameters`() {
        val now = Instant.now()
        val previousKaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomojiDto = KaomojiDto(createdAt = now, updatedAt = now, key = "newKey", emoticon = "newEmoticon")
        val tags = listOf(
            Tag(id = 1L, label = "label1"),
            Tag(id = 3L, label = "label3")
        )

        val kaomoji = KaomojiDto.replace(previousKaomoji, kaomojiDto, tags)

        assertEquals(1L, kaomoji.id)
        assertNull(kaomoji.createdAt)
        assertNull(kaomoji.updatedAt)
        assertEquals(kaomojiDto.key, kaomoji.key)
        assertEquals(kaomojiDto.emoticon, kaomoji.emoticon)
        assertEquals(2, kaomoji.tags.size)
        assertTrue(kaomoji.tags.containsAll(tags))
    }

    @Test
    fun `update - update Kaomoji object with valid parameters (no data)`() {
        val tags = listOf(
            Tag(id = 1L, label = "label1"),
            Tag(id = 2L, label = "label2")
        )
        val previousKaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = tags
        )
        val kaomojiDto = KaomojiDto()

        val kaomoji = KaomojiDto.update(previousKaomoji, kaomojiDto, null)

        assertEquals(previousKaomoji.key, kaomoji.key)
        assertEquals(previousKaomoji.emoticon, kaomoji.emoticon)
        assertEquals(2, kaomoji.tags.size)
        assertTrue(kaomoji.tags.containsAll(tags))
    }

    @Test
    fun `update - update Kaomoji object with valid parameters`() {
        val now = Instant.now()
        val previousKaomoji = Kaomoji(
            id = 1L,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )
        val kaomojiDto = KaomojiDto(createdAt = now, updatedAt = now, key = "newKey", emoticon = "newEmoticon")
        val tags = listOf(
            Tag(id = 1L, label = "label1"),
            Tag(id = 3L, label = "label3")
        )

        val kaomoji = KaomojiDto.update(previousKaomoji, kaomojiDto, tags)

        assertNull(kaomoji.createdAt)
        assertNull(kaomoji.updatedAt)
        assertEquals(kaomojiDto.key, kaomoji.key)
        assertEquals(kaomojiDto.emoticon, kaomoji.emoticon)
        assertEquals(2, kaomoji.tags.size)
        assertTrue(kaomoji.tags.containsAll(tags))
    }

    @Test
    fun `toDomain - create KaomojiDto object with valid parameters`() {
        val now = Instant.now()
        val kaomoji = Kaomoji(
            id = 1L,
            createdAt = now,
            updatedAt = now,
            key = "key",
            emoticon = "emoticon",
            tags = listOf(
                Tag(id = 1L, label = "label1"),
                Tag(id = 2L, label = "label2")
            )
        )

        val kaomojiDto = KaomojiDto.toDomain(kaomoji)

        assertEquals(kaomoji.id, kaomojiDto.id)
        assertEquals(kaomoji.createdAt, kaomojiDto.createdAt)
        assertEquals(kaomoji.updatedAt, kaomojiDto.updatedAt)
        assertEquals(kaomoji.key, kaomojiDto.key)
        assertEquals(kaomoji.emoticon, kaomojiDto.emoticon)
        assertTrue(kaomoji.tags.containsAll(kaomoji.tags))
    }
}