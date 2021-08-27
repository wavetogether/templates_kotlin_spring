package com.wavetogether.core.domain.user.exception

import com.wavetogether.core.domain.user.UserExceptions
import com.wavetogether.core.exception.WaveException
import java.util.*

class UserByKeyNotFoundException(
  key: UUID,
  override val cause: Throwable? = null,
) : WaveException("User with key '$key' is not found.", cause, UserExceptions.USER_BY_KEY_NOT_FOUND)
