package com.wavetogether.core.exception

open class ResourceNotFoundException(
  override val message: String? = null,
  override val cause: Throwable? = null,
  situation: ExceptionalSituation
): WaveException(message, cause, situation)
