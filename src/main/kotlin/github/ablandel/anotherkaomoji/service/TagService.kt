package github.ablandel.anotherkaomoji.service

import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.dto.v1.TagDto
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.DataAlreadyExistException
import github.ablandel.anotherkaomoji.exception.DataNotFoundException
import github.ablandel.anotherkaomoji.exception.InconsistentParameterException
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import github.ablandel.anotherkaomoji.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(val tagRepository: TagRepository) {

    fun findWithPagination(paginationDto: PaginationDto): List<TagDto> {
        val tags =
            if (paginationDto.cursor != null) tagRepository.findWithLimitAndCursor(
                paginationDto.cursor,
                paginationDto.limit
            ) else tagRepository.findWithLimitAndOffset(paginationDto.offset, paginationDto.limit)
        return tags.map { TagDto.toDomain(it) }.toList()
    }

    fun count(): Long {
        return tagRepository.count()
    }

    private fun findTagById(id: Long): Tag {
        val tag = tagRepository.findById(id)
        if (tag.isPresent) {
            return tag.get()
        } else {
            throw DataNotFoundException("id", id.toString())
        }
    }

    fun findById(id: Long): TagDto {
        val tag = findTagById(id)
        return TagDto.toDomain(tag)
    }

    @Transactional
    fun deleteById(id: Long) {
        findTagById(id) // Used to check if the ID exists
        tagRepository.deleteById(id)
    }

    @Transactional
    fun create(tagDto: TagDto): TagDto {
        if (tagDto.label.isNullOrBlank()) {
            throw InvalidParameterException("Invalid parameters: [`label`]")
        }
        val tag = tagRepository.findByLabelIgnoreCase(tagDto.label)
        return if (tag.isEmpty) {
            TagDto.toDomain(tagRepository.save(TagDto.fromDomain(tagDto)))
        } else {
            throw DataAlreadyExistException("label", tagDto.label)
        }
    }

    fun prepare(tagsLabels: List<String>): List<Tag> {
        val uniqueTags = tagsLabels.toSet()
        val tags = tagRepository.findByLabelInIgnoreCase(uniqueTags.toList()).toMutableList()
        if (uniqueTags.size != tags.size) {
            val newTags = mutableListOf<Tag>()
            for (tag in uniqueTags) {
                if (tags.none { t -> t.label.lowercase() == tag.lowercase() }) {
                    newTags.add(Tag(label = tag))
                }
            }
            tags.addAll(tagRepository.saveAll(newTags))
        }
        return tags
    }

    private fun checkIfUniqueOrThrow(tagDto: TagDto, tag: Tag) {
        if (tagDto.label != null) {
            val otherTag = tagRepository.findByLabelIgnoreCase(tagDto.label)
            if (otherTag.isPresent && tag.id != otherTag.get().id) {
                throw DataAlreadyExistException("label", tagDto.label)
            }
        }
    }

    @Transactional
    private fun replaceOrUpdate(
        id: Long,
        tagDto: TagDto,
        mapping: (Tag, TagDto) -> Tag
    ): TagDto {
        if (tagDto.id != null && id != tagDto.id) {
            throw InconsistentParameterException("id")
        }
        if (tagDto.label != null && tagDto.label.isBlank()) {
            throw InvalidParameterException("Invalid parameters: [`label`]")
        }
        // Before replacing or updating, check if the label is already used by another tag.
        val tag = findTagById(id)
        checkIfUniqueOrThrow(tagDto, tag)
        return TagDto.toDomain(tagRepository.save(mapping(tag, tagDto)))
    }

    fun replace(
        id: Long,
        tagDto: TagDto
    ): TagDto {
        return replaceOrUpdate(id, tagDto, TagDto::replace)
    }

    fun update(
        id: Long,
        tagDto: TagDto
    ): TagDto {
        return replaceOrUpdate(id, tagDto, TagDto::update)
    }
}
