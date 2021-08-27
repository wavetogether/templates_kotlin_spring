package com.wavetogether.core.service.user.impl

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.domain.user.impl.EditableUserVo
import com.wavetogether.core.service.user.CreateUserService
import java.util.*

internal class CreateUserServiceImpl(
  private val users: UserRepository
) : CreateUserService {
  override fun createUser(name: String): User {
    val newUser = EditableUserVo(
      key = UUID.randomUUID(),
      name = name
    )

    return this.users.save(newUser)
  }
}
