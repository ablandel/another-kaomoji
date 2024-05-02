package github.ablandel.anotherkaomoji.repository

import github.ablandel.anotherkaomoji.entity.Tag
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface
TagRepository : CrudRepository<Tag, Long> {
    @Query("SELECT t FROM tag t ORDER BY id LIMIT :limit OFFSET :offset")
    fun findWithLimitAndOffset(offset: Int, limit: Int): List<Tag>

    @Query("SELECT t FROM tag t WHERE t.id > :cursor ORDER BY id LIMIT :limit")
    fun findWithLimitAndCursor(cursor: Long, limit: Int): List<Tag>

    fun findByLabelIgnoreCase(label: String): Optional<Tag>
    fun findByLabelInIgnoreCase(labels: List<String>): List<Tag>
}
