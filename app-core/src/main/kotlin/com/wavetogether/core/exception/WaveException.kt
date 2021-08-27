package com.wavetogether.core.exception

abstract class WaveException(
  override val message: String?,
  override val cause: Throwable?,
  val situation: ExceptionalSituation
) : Exception(message, cause)
