package github.ablandel.anotherkaomoji.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.Instant

@Entity(name = "kaomoji")
data class Kaomoji(
    val key: String,
    val emoticon: String,
    override val id: Long? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    @ManyToMany(cascade = [CascadeType.PERSIST])
    @BatchSize(size = TAGS_BATCH_SIZE)
    @JoinTable(
        name = "kaomojis_tags",
        joinColumns = [JoinColumn(name = "kaomoji_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: List<Tag>,
) : AbstractEntity() {
    companion object {
        const val TAGS_BATCH_SIZE = 20
    }
}
