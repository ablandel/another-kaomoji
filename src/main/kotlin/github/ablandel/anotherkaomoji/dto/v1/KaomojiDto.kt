package github.ablandel.anotherkaomoji.dto.v1

import github.ablandel.anotherkaomoji.dto.Validation
import github.ablandel.anotherkaomoji.entity.Kaomoji
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.PositiveOrZero
import java.time.Instant

data class KaomojiDto(
    @field:Null(groups = [Validation.Create::class])
    @field:PositiveOrZero(groups = [Validation.Replace::class, Validation.Update::class])
    val id: Long? = null,
    @field:NotNull(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    @field:NotBlank(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val key: String? = null,
    @field:NotNull(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    @field:NotBlank(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val emoticon: String? = null,
    @field:NotNull(groups = [Validation.Create::class, Validation.Replace::class])
    val tags: List<TagDto>? = null,
    @field:Null(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val createdAt: Instant? = null,
    @field:Null(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val updatedAt: Instant? = null
) {
    companion object {
        fun fromDomain(
            kaomojiDto: KaomojiDto,
            tags: List<Tag>
        ): Kaomoji {
            if (kaomojiDto.key.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`key`]")
            }
            if (kaomojiDto.emoticon.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`emoticon`]")
            }
            return Kaomoji(
                key = kaomojiDto.key,
                emoticon = kaomojiDto.emoticon,
                tags = tags
            )
        }

        fun replace(
            from: Kaomoji,
            to: KaomojiDto,
            tags: List<Tag>
        ): Kaomoji {
            if (to.key.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`key`]")
            }
            if (to.emoticon.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`emoticon`]")
            }
            return Kaomoji(
                id = from.id,
                createdAt = from.createdAt,
                key = to.key,
                emoticon = to.emoticon,
                tags = tags
            )
        }

        fun update(
            from: Kaomoji,
            to: KaomojiDto,
            tags: List<Tag>?
        ): Kaomoji {
            return Kaomoji(
                id = from.id,
                createdAt = from.createdAt,
                key = if (!to.key.isNullOrBlank()) to.key else from.key,
                emoticon = if (!to.emoticon.isNullOrBlank()) to.emoticon else from.emoticon,
                tags = tags ?: from.tags,
            )
        }

        fun toDomain(kaomoji: Kaomoji): KaomojiDto {
            return KaomojiDto(
                id = kaomoji.id,
                createdAt = kaomoji.createdAt,
                updatedAt = kaomoji.updatedAt,
                key = kaomoji.key,
                emoticon = kaomoji.emoticon,
                tags = kaomoji.tags.map { TagDto.toDomain(it) }.toList()
            )
        }
    }
}