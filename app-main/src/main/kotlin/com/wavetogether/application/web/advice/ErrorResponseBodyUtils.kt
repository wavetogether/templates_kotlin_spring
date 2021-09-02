package com.wavetogether.application.web.advice

import com.wavetogether.AppProfile
import com.wavetogether.BuildConfig
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException
import javax.servlet.http.HttpServletRequest

internal fun logError(log: Logger, req: HttpServletRequest, message: String, ex: Exception) {
  if (BuildConfig.currentProfile == AppProfile.RELEASE) {
    // Minimise log outputs in RELEASE binary
    log.error("{} {}: {}", req.method, req.requestURI, message)
    logCauses(log, req, ex)
  } else {
    log.error("{} {}: {}", req.method, req.requestURI, message, ex)
  }
}

internal fun logCauses(log: Logger, req: HttpServletRequest, cause: Throwable?) {
  if (cause == null) {
    return
  } else {
    val superCause = cause.cause
    if (superCause == null) {
      log.error("""{} {}: {} ("{}")""", req.method, req.requestURI, cause::class, cause.message)
    } else {
      log.error("by {}", cause::class)
      logCauses(log, req, superCause)
    }
  }
}

internal fun Throwable.toHttpStatus(): HttpStatus = when (this) {
  is HttpStatusCodeException -> this.statusCode
  else -> this.cause?.toHttpStatus() ?: HttpStatus.INTERNAL_SERVER_ERROR
}

internal fun Throwable.simpleName(): String = this::class.simpleName ?: ""
