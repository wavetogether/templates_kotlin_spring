package com.wavetogether.endpoint.v1.users.createUser

import com.wavetogether.endpoint.v1.users.CreateUserController
import com.wavetogether.core.service.user.CreateUserService
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import org.slf4j.Logger
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateUserControllerImpl(
  private val log: Logger,
  private val svc: CreateUserService
) : CreateUserController {
  override fun createUser(req: CreateUserRequestImpl): UserResponseImpl {
    this.log.debug("createUser: {}", req)

    val createdUser = this.svc.createUser(req.name)

    return UserResponseImpl.from(createdUser)
  }
}
