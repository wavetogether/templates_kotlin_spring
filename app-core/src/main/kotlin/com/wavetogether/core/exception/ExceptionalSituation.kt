package com.wavetogether.core.exception

import kotlin.reflect.KClass

interface ExceptionalSituation {
  val code: Int

  val type: KClass<out WaveException>
}
