package com.wavetogether.infrastructure.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.transaction.Transactional

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
  @Query(
    """
    SELECT u
    FROM UserJpaEntity u
    WHERE u.key = ?1
  """
  )
  fun findByKey(key: UUID): UserJpaEntity?

  @Modifying
  @Query(
    """
    DELETE FROM UserJpaEntity u
    WHERE u.key = ?1
  """
  )
  @Transactional
  fun deleteByKey(key: UUID): Int
}
