package com.wavetogether.application.web.advice

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wavetogether.endpoint.common.response.AbstractGenericResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * This class wraps all [com.fasterxml.jackson.databind.annotation.JsonSerialize] annotated values as
 * [com.wavetogether.endpoint._common.response.GenericOkResponse].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Oct - 2018
 */
@RestControllerAdvice
class OkResponseBodyDecorator : ResponseBodyAdvice<Any> {
  override fun supports(
    returnType: MethodParameter,
    converterType: Class<out HttpMessageConverter<*>>
  ): Boolean {
    val methodReturnType = returnType.method?.returnType ?: return false
    return methodReturnType.declaredAnnotations.any {
      it.annotationClass == JsonSerialize::class
    }
  }

  override fun beforeBodyWrite(
    body: Any?,
    returnType: MethodParameter,
    selectedContentType: MediaType,
    selectedConverterType: Class<out HttpMessageConverter<*>>,
    request: ServerHttpRequest,
    response: ServerHttpResponse
  ): Any? {
    if (body == null) {
      return null
    }

    return AbstractGenericResponse.ok(body)
  }
}
