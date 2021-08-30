package com.wavetogether.appConfig.web.advice

import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RequestBodyDecorator {
  /**
   * Trims all String type requests
   */
  @InitBinder
  fun registerStringTrimmer(binder: WebDataBinder) {
    binder.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
  }
}
