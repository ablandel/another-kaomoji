package github.ablandel.anotherkaomoji.entity

import jakarta.persistence.Entity
import java.time.Instant

@Entity(name = "tag")
data class Tag(
    val label: String,
    override val id: Long? = null,
    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
) : AbstractEntity()
