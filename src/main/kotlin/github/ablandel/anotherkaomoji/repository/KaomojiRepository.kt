package github.ablandel.anotherkaomoji.repository

import github.ablandel.anotherkaomoji.entity.Kaomoji
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface KaomojiRepository : CrudRepository<Kaomoji, Long> {
    @Query("SELECT k FROM kaomoji k ORDER BY id LIMIT :limit OFFSET :offset")
    fun findWithLimitAndOffset(offset: Int, limit: Int): List<Kaomoji>

    @Query("SELECT k FROM kaomoji k WHERE k.id > :cursor ORDER BY id LIMIT :limit")
    fun findWithLimitAndCursor(cursor: Long, limit: Int): List<Kaomoji>

    @Query("SELECT k FROM kaomoji k WHERE LOWER(k.key) = LOWER(:key) OR k.emoticon = :emoticon")
    fun findByKeyOrEmoticon(key: String, emoticon: String): List<Kaomoji>

    fun findByKeyIgnoreCase(key: String): Optional<Kaomoji>
    fun findByEmoticon(emoticon: String): Optional<Kaomoji>
}
