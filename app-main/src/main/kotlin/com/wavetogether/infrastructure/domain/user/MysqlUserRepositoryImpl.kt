package com.wavetogether.infrastructure.domain.user

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MysqlUserRepositoryImpl(
  private val jpaDao: UserJpaRepository
) : UserRepository {
  override fun findById(id: Long): User? = this.jpaDao.findByIdOrNull(id)

  override fun findByKey(key: UUID): User? = this.jpaDao.findByKey(key)

  override fun save(value: User): User.Persisted = this.jpaDao.save(UserJpaEntity.create(value)).asPersisted()

  override fun deleteById(id: Long): Int {
    this.jpaDao.deleteById(id)
    return 1
  }

  override fun deleteByKey(key: UUID): Int {
    this.jpaDao.deleteByKey(key)
    return 1
  }
}
