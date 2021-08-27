package com.wavetogether.core.domain.user

import java.util.*

interface UserRepository {
  fun findById(id: Long): User?

  fun findByKey(key: UUID): User?

  fun save(value: User): User.Persisted

  fun delete(user: User): Int = this.deleteById(
    user.id ?: throw IllegalArgumentException("This object is not under a persistent state.")
  )

  fun deleteById(id: Long): Int

  fun deleteByKey(key: UUID): Int
}
