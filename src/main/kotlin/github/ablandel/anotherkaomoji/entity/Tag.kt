package github.ablandel.anotherkaomoji.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import java.time.Instant

@Entity(name = "tag")
data class Tag(
    val label: String,
    override val id: Long? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    @ManyToMany(mappedBy = "tags")
    val kaomojis: Set<Kaomoji>? = null,
) : AbstractEntity()
