package com.wavetogether.endpoint.v1.users.getUser

import com.wavetogether.core.service.user.SearchUserService
import com.wavetogether.endpoint.v1.users.GetUserController
import com.wavetogether.endpoint.v1.users.common.UserResponseImpl
import org.slf4j.Logger
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GetUserControllerImpl(
  private val log: Logger,
  private val svc: SearchUserService
) : GetUserController {
  override fun getUser(userKey: String): UserResponseImpl {
    this.log.debug("getUser: {}", userKey)

    val user = this.svc.getByKey(UUID.fromString(userKey))

    return UserResponseImpl.from(user)
  }
}
