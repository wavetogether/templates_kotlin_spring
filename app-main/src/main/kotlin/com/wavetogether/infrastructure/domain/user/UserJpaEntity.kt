package com.wavetogether.infrastructure.domain.user

import com.wavetogether.core.domain.user.User
import com.wavetogether.infrastructure.converter.ByteArrayUuidConverter
import com.wavetogether.lib.util.UuidExtensions
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class UserJpaEntity : User.Editable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  override var id: Long? = 0L

  @Convert(converter = ByteArrayUuidConverter::class)
  @Column(nullable = false, columnDefinition = "VARBINARY(16)")
  override var key: UUID = UuidExtensions.EMPTY_UUID

  @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
  override var name: String = ""

  override fun edit(): User.Editable = this

  fun asPersisted(): User.Persisted {
    val myId = this.id ?: throw IllegalStateException("This entity object is not persisted")

    return object : User.Persisted {
      override val id: Long = myId
      override val key: UUID = this@UserJpaEntity.key
      override val name: String = this@UserJpaEntity.name
      override fun edit(): User.Editable {
        throw UnsupportedOperationException("Cannot edit persisted entity object")
      }
    }
  }

  override fun toString() = """UserJpaEntity(id=$id,
    | key=$key, 
    | name='$name'
    |)""".trimMargin()

  companion object {
    fun create(src: User) = UserJpaEntity().apply {
      this.id = src.id
      this.key = src.key
      this.name = src.name
    }
  }
}
