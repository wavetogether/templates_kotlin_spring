package com.wavetogether.appConfig.web.advice

import com.fasterxml.jackson.core.JsonProcessingException
import com.wavetogether.AppProfile
import com.wavetogether.BuildConfig
import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import org.slf4j.Logger
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Controller
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.ValidationException

/**
 * This only works if following settings are applied:
 * ```
 * spring:
 *   mvc:
 *     throw-exception-if-no-handler-found: true
 *   resources:
 *     add-mappings: false
 * ```
 * Read [this post](https://stackoverflow.com/questions/28902374/spring-boot-rest-service-exception-handling/30193013)
 * for more information.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
@Controller
@RestControllerAdvice
class SpringErrorResponseBodyDecorator(
  private val log: Logger
) : ErrorController {
  // Suppose that javax.validation is used only on Controller methods.
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodBindingException(req: HttpServletRequest, ex: MethodArgumentNotValidException): ResponseEntity<GenericErrorResponse> {
    this.log.error("{} {}: Illegal request from client. Constraint violations are:", req.method, req.requestURI)
    logError(req, "Spring unhandled exception:", ex)

    val msg = ex.bindingResult.allErrors.map {
      this.log.error("  {}", it.defaultMessage)
      return@map it.defaultMessage
    }.joinToString()

    val response = AbstractGenericResponse.error(msg, ex.simpleName())
    return ResponseEntity(response, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(
    HttpMediaTypeNotSupportedException::class,
    HttpMessageNotReadableException::class,
    JsonProcessingException::class,
    ValidationException::class
  )
  fun handleSpring400(req: HttpServletRequest, ex: Exception): ResponseEntity<GenericErrorResponse> {
    logError(req, "Spring unhandled exception:", ex)

    val response = AbstractGenericResponse.error(
      "Cannot process given request.", ex.simpleName()
    )

    return ResponseEntity(response, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(NoHandlerFoundException::class, HttpRequestMethodNotSupportedException::class)
  fun handleSpring404(req: HttpServletRequest, ex: Exception): ResponseEntity<GenericErrorResponse> {
    logError(req, "Spring unhandled exception:", ex)

    val response = AbstractGenericResponse.error(
      "Resource '${req.requestURI ?: ""}' is not found.", ex.simpleName()
    )

    return ResponseEntity(response, HttpStatus.NOT_FOUND)
  }

  /**
   * Any errors, that happen on the outside of Spring Context - exceptions in Servlet filters
   * for example, are redirected to this method to decorate error output as our own favour,
   * rather than Spring's [org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController].
   */
  @RequestMapping(ApiPaths.ERROR)
  fun handleError(request: HttpServletRequest): ResponseEntity<GenericErrorResponse> {
    val exception = (request.getAttribute("javax.servlet.error.exception") as? Exception)
      ?: HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
    val response = AbstractGenericResponse.error("Internal server error.", exception.simpleName())

    logError(request, "Spring unhandled exception:", exception)

    return ResponseEntity(response, exception.toHttpStatus())
  }

  private fun logError(req: HttpServletRequest, message: String, ex: Exception) {
    if (BuildConfig.currentProfile == AppProfile.RELEASE) {
      // Minimise log outputs in RELEASE binary
      this.log.error("{} {}: {}", req.method, req.requestURI, message)
      this.logCauses(req, ex)
    } else {
      this.log.error("{} {}: {}", req.method, req.requestURI, message, ex)
    }
  }

  private fun logCauses(req: HttpServletRequest, cause: Throwable?) {
    if (cause == null) {
      return
    } else {
      val superCause = cause.cause
      if (superCause == null) {
        this.log.error("""{} {}: {} ("{}")""", req.method, req.requestURI, cause::class, cause.message)
      } else {
        this.log.error("by {}", cause::class)
        this.logCauses(req, superCause)
      }
    }
  }

  private fun Throwable.toHttpStatus(): HttpStatus = when (this) {
    is HttpStatusCodeException -> this.statusCode
    else -> this.cause?.toHttpStatus() ?: HttpStatus.INTERNAL_SERVER_ERROR
  }

  private fun Throwable.simpleName(): String = this::class.simpleName ?: ""
}
