package com.wavetogether.application.web.advice

import com.wavetogether.core.exception.ResourceNotFoundException
import com.wavetogether.core.exception.WaveException
import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpServerErrorException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class WaveExceptionErrorResponseBodyDecorator(
  private val log: Logger
) {
  @ExceptionHandler(WaveException::class)
  fun onWaveException(req: HttpServletRequest, ex: WaveException): ResponseEntity<GenericErrorResponse> {
    logError(this.log, req, "Core logic exception:", ex)

    val (response, status) = when (ex) {
      is ResourceNotFoundException -> errorResponse(ex.message, ex.simpleName(), HttpStatus.NOT_FOUND)
      else -> errorResponse(
        "Error is undefined.",
        HttpServerErrorException.InternalServerError::class.simpleName ?: "",
        HttpStatus.INTERNAL_SERVER_ERROR
      )
    }

    return ResponseEntity(response, status)
  }

  private fun errorResponse(
    message: String?,
    cause: String,
    status: HttpStatus
  ): Pair<GenericErrorResponse, HttpStatus> = AbstractGenericResponse.error(message ?: "", cause) to status
}
