package com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.endpoint.v1.users.createUser.CreateUserRequestImpl
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.Valid

@RequestMapping(
  produces = [MediaType.APPLICATION_JSON_VALUE],
  consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface CreateUserController {
  @RequestMapping(
    path = [ApiPaths.USERS],
    method = [RequestMethod.POST]
  )
  fun createUser(@Valid @RequestBody req: CreateUserRequestImpl): UserResponseImpl
}
