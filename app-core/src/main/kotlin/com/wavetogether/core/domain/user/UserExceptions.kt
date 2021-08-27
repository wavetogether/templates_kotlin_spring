package com.wavetogether.core.domain.user

import com.wavetogether.core.domain.user.exception.UserByIdNotFoundException
import com.wavetogether.core.domain.user.exception.UserByKeyNotFoundException
import com.wavetogether.core.exception.ExceptionalSituation
import com.wavetogether.core.exception.WaveException
import kotlin.reflect.KClass

enum class UserExceptions(
  override val code: Int,
  override val type: KClass<out WaveException>
) : ExceptionalSituation {
  USER_BY_ID_NOT_FOUND(1, UserByIdNotFoundException::class),
  USER_BY_KEY_NOT_FOUND(2, UserByKeyNotFoundException::class),
}
