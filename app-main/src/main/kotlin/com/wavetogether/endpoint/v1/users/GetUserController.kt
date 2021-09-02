package com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.lib.text.REGEX_UUID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.constraints.Pattern

@RequestMapping(
  produces = [MediaType.APPLICATION_JSON_VALUE],
  consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface GetUserController {
  @RequestMapping(
    path = [ApiPaths.USERS_KEY],
    method = [RequestMethod.GET]
  )
  fun getUser(
    @Pattern(
      regexp = REGEX_UUID,
      message = "`userKey` must be in a UUID format."
    )
    @PathVariable
    userKey: String,
  ): UserResponseImpl
}
