package github.ablandel.anotherkaomoji.entity

import jakarta.persistence.*
import java.time.Instant
import java.time.temporal.ChronoUnit

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column(updatable = false)
    open var createdAt: Instant? = null
    open var updatedAt: Instant? = null

    private fun nowWithMillisPrecision(): Instant {
        return Instant.now().truncatedTo(ChronoUnit.MILLIS)
    }

    @PrePersist
    fun prePersist() {
        val now = nowWithMillisPrecision()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = nowWithMillisPrecision()
    }
}