package com.wavetogether.appConfig.web

import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.infrastructure.web.RequestLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestInterceptorsConfig : WebMvcConfigurer {
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(RequestLoggingInterceptor())
      .addPathPatterns("${ApiPaths.V_ALL}/**")
  }
}
