package github.ablandel.anotherkaomoji.service

import github.ablandel.anotherkaomoji.dto.v1.KaomojiDto
import github.ablandel.anotherkaomoji.dto.v1.PaginationDto
import github.ablandel.anotherkaomoji.entity.Kaomoji
import github.ablandel.anotherkaomoji.entity.Tag
import github.ablandel.anotherkaomoji.exception.DataAlreadyExistException
import github.ablandel.anotherkaomoji.exception.DataNotFoundException
import github.ablandel.anotherkaomoji.exception.InconsistentParameterException
import github.ablandel.anotherkaomoji.exception.InvalidParameterException
import github.ablandel.anotherkaomoji.repository.KaomojiRepository
import org.springframework.stereotype.Service

@Service
class KaomojiService(val kaomojiRepository: KaomojiRepository, val tagService: TagService) {

    fun findWithPagination(paginationDto: PaginationDto): List<KaomojiDto> {
        val kaomojis =
            if (paginationDto.cursor != null) kaomojiRepository.findWithLimitAndCursor(
                paginationDto.cursor,
                paginationDto.limit
            ) else kaomojiRepository.findWithLimitAndOffset(paginationDto.offset, paginationDto.limit)
        return kaomojis.map { KaomojiDto.toDomain(it) }.toList()
    }

    fun count(): Long {
        return kaomojiRepository.count()
    }

    private fun findKaomojiById(id: Long): Kaomoji {
        val kaomoji = kaomojiRepository.findById(id)
        if (kaomoji.isPresent) {
            return kaomoji.get()
        } else {
            throw DataNotFoundException("id", id.toString())
        }
    }

    fun findById(id: Long): KaomojiDto {
        return KaomojiDto.toDomain(findKaomojiById(id))
    }

    fun deleteById(id: Long) {
        findKaomojiById(id) // Used to check if the ID exists
        kaomojiRepository.deleteById(id)
    }

    fun create(kaomojiDto: KaomojiDto): KaomojiDto {
        if (kaomojiDto.key.isNullOrBlank()) {
            throw InvalidParameterException("Invalid parameters: [`key`]")
        }
        if (kaomojiDto.emoticon.isNullOrBlank()) {
            throw InvalidParameterException("Invalid parameters: [`emoticon`]")
        }
        if (kaomojiDto.tags == null) {
            throw InvalidParameterException("Invalid parameters: [`tags`]")
        }
        val kaomojis = kaomojiRepository.findByKeyOrEmoticon(kaomojiDto.key, kaomojiDto.emoticon)
        if (kaomojis.isEmpty()) {
            return KaomojiDto.toDomain(
                kaomojiRepository.save(
                    KaomojiDto.fromDomain(
                        kaomojiDto,
                        tagService.prepare(kaomojiDto.tags.filter { it -> it.label != null }.map { it -> it.label!! })
                            .toList()
                    )
                )
            )
        } else {
            throw DataAlreadyExistException("Kaomoji cannot be created with key `${kaomojiDto.key}` and emoticon `${kaomojiDto.emoticon}` - key or emoticon already used for another kaomoji(s)")
        }
    }

    private fun replaceOrUpdate(
        id: Long,
        kaomojiDto: KaomojiDto,
        mapping: (Kaomoji, KaomojiDto, List<Tag>) -> Kaomoji
    ): KaomojiDto {
        if (kaomojiDto.id != null && id != kaomojiDto.id) {
            throw InconsistentParameterException("id")
        }
        if (kaomojiDto.key != null && kaomojiDto.key.isBlank()) {
            throw InvalidParameterException("Invalid parameters: [`key`]")
        }
        if (kaomojiDto.emoticon != null && kaomojiDto.emoticon.isBlank()) {
            throw InvalidParameterException("Invalid parameters: [`emoticon`]")
        }
        val kaomoji = findKaomojiById(id)
        val tags = if (kaomojiDto.tags != null) tagService.prepare(kaomojiDto.tags.filter { it -> it.label != null }
            .map { it -> it.label!! }).toList()
        else kaomoji.tags
        return KaomojiDto.toDomain(
            kaomojiRepository.save(
                mapping(
                    kaomoji,
                    kaomojiDto,
                    tags
                )
            )
        )
    }

    fun replace(
        id: Long,
        kaomojiDto: KaomojiDto
    ): KaomojiDto {
        return replaceOrUpdate(id, kaomojiDto, KaomojiDto::replace)
    }

    fun update(
        id: Long,
        kaomojiDto: KaomojiDto
    ): KaomojiDto {
        return replaceOrUpdate(id, kaomojiDto, KaomojiDto::update)
    }
}
