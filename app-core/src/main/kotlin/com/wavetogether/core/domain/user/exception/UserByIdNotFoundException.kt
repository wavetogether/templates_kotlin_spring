package com.wavetogether.core.domain.user.exception

import com.wavetogether.core.domain.user.UserExceptions
import com.wavetogether.core.exception.ResourceNotFoundException
import com.wavetogether.core.exception.WaveException

class UserByIdNotFoundException(
  id: Long,
  override val cause: Throwable? = null,
) : ResourceNotFoundException("User with id '$id' is not found.", cause, UserExceptions.USER_BY_ID_NOT_FOUND)
