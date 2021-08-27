package com.wavetogether.core.service.user

import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.domain.user.exception.UserByIdNotFoundException
import com.wavetogether.core.domain.user.exception.UserByKeyNotFoundException
import com.wavetogether.core.service.user.impl.SearchUserServiceImpl
import java.util.*

interface SearchUserService {
  fun getById(userId: Long): User =
    this.findById(userId) ?: throw UserByIdNotFoundException(userId)

  fun getByKey(key: UUID): User =
    this.findByKey(key) ?: throw UserByKeyNotFoundException(key)

  fun findById(userId: Long): User?

  fun findByKey(key: UUID): User?

  companion object {
    fun newInstance(userRepository: UserRepository): SearchUserService =
      SearchUserServiceImpl(userRepository)
  }
}
