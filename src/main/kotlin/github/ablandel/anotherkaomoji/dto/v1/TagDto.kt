package github.ablandel.anotherkaomoji.dto.v1

import github.ablandel.anotherkaomoji.dto.Validation
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.PositiveOrZero
import java.time.Instant

data class TagDto(
    @field:Null(groups = [Validation.Create::class])
    @field:PositiveOrZero(groups = [Validation.Replace::class, Validation.Update::class])
    val id: Long? = null,
    @field:NotNull(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    @field:NotBlank(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val label: String? = null,
    @field:Null(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val createdAt: Instant? = null,
    @field:Null(groups = [Validation.Create::class, Validation.Replace::class, Validation.Update::class])
    val updatedAt: Instant? = null
) {
    companion object {
        fun fromDomain(tagDto: TagDto): Tag {
            if (tagDto.label.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`label`]")
            }
            return Tag(
                label = tagDto.label
            )
        }

        fun replace(from: Tag, to: TagDto): Tag {
            if (to.label.isNullOrBlank()) {
                throw InvalidParameterException("Invalid parameters: [`label`]")
            }
            return Tag(
                id = from.id,
                createdAt = from.createdAt,
                label = to.label,
            )
        }

        fun update(from: Tag, to: TagDto): Tag {
            return Tag(
                id = from.id,
                createdAt = from.createdAt,
                label = if (!to.label.isNullOrBlank()) to.label else from.label,
            )
        }

        fun toDomain(tag: Tag): TagDto {
            return TagDto(
                id = tag.id,
                createdAt = tag.createdAt,
                updatedAt = tag.updatedAt,
                label = tag.label
            )
        }
    }
}