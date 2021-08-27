package com.wavetogether.infrastruture.domain.user

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.lib.annotation.VisibleForTesting
import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal class MemoryUserRepositoryImpl : UserRepository {
  private val userIdMap = ConcurrentHashMap<Long, User>()
  private val userKeyMap = ConcurrentHashMap<UUID, User>()

  override fun findById(id: Long): User? = this.userIdMap[id]

  override fun findByKey(key: UUID): User? = this.userKeyMap[key]

  override fun save(value: User): User.Persisted = synchronized(LOCK_USER_MAP) {
    val idAssignedUser = if (value.id === null) {
      value.edit().apply {
        this.id = userIdMap.size + 1L
      }
    } else {
      value
    }

    return PersistedUser.from(idAssignedUser).also {
      this.userIdMap[it.id] = it
      this.userKeyMap[it.key] = it
    }
  }

  override fun deleteById(id: Long): Int =
    this.deleteInternal(this.userIdMap[id])

  override fun deleteByKey(key: UUID): Int =
    this.deleteInternal(this.userKeyMap[key])

  @VisibleForTesting
  fun count(): Int = synchronized(LOCK_USER_MAP) {
    if (this.userIdMap.size != this.userKeyMap.size) {
      throw IllegalStateException("Persisted user must have id.")
    }

    return this.userIdMap.size
  }

  private fun deleteInternal(persistedUser: User?): Int {
    if (persistedUser == null) {
      return 0
    }

    val userId = persistedUser.id ?: throw IllegalStateException("Persisted user must have id.")

    synchronized(LOCK_USER_MAP) {
      this.userIdMap.remove(userId)
      this.userKeyMap.remove(persistedUser.key)
    }

    return 1
  }

  companion object {
    private val LOCK_USER_MAP = Any()
  }
}

private data class PersistedUser(
  override val id: Long,
  override val key: UUID,
  override val name: String
) : User.Persisted {
  companion object {
    fun from(src: User) = PersistedUser(
      id = src.id ?: throw IllegalStateException("No id is assigned to user object to be assigned"),
      key = src.key,
      name = src.name
    )
  }

  override fun edit(): User.Editable =
    throw UnsupportedOperationException("Must not be called")
}
