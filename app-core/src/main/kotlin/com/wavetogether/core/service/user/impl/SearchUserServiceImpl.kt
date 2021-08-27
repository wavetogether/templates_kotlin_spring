package com.wavetogether.core.service.user.impl

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.service.user.SearchUserService
import java.util.*

internal class SearchUserServiceImpl(
  private val users: UserRepository
) : SearchUserService {
  override fun findById(userId: Long): User? = this.users.findById(userId)

  override fun findByKey(key: UUID): User? = this.users.findByKey(key)
}
