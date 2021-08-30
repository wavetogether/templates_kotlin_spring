package com.wavetogether.appConfig.web

import com.wavetogether.infrastructure.web.extractIpStr
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 */
class RequestLoggingInterceptor : HandlerInterceptor {
  override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
    val ip = req.run { "${extractIpStr()} ($remoteAddr:$remotePort)" }
    val method = req.method.let {
      it.padEnd(MAX_HTTP_METHOD_LENGTH - it.length, ' ')
    }
    val path = req.servletPath
    val handlerInfo = if (handler is HandlerMethod) {
      val hm = handler.method
      val className = hm.declaringClass.canonicalName
      val methodName = hm.name
      String.format("%s#%s", className, methodName)
    } else {
      handler.javaClass.toString()
    }

    LOG.info("{} {} from {} << {}", method, path, ip, handlerInfo)
    val headers = req.headerNames

    while (headers.hasMoreElements()) {
      val header = headers.nextElement()
      val value = req.getHeader(header)
      LOG.debug("{} : {}", header, value)
    }

    return super.preHandle(req, resp, handler)
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)

    // DELETE
    private const val MAX_HTTP_METHOD_LENGTH = 6
  }
}
