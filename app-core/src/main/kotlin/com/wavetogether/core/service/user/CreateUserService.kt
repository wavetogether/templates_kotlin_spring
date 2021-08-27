package com.wavetogether.core.service.user

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.service.user.impl.CreateUserServiceImpl

interface CreateUserService {
  fun createUser(name: String): User

  companion object {
    fun newInstance(userRepository: UserRepository): CreateUserService =
      CreateUserServiceImpl(userRepository)
  }
}
