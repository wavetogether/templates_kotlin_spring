package com.wavetogether.appConfig.core.service

import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.service.user.CreateUserService
import com.wavetogether.core.service.user.SearchUserService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class UserServiceBeans {
  @Bean
  fun createUserService(userRepository: UserRepository): CreateUserService =
    CreateUserService.newInstance(userRepository)

  @Bean
  fun searchUserService(userRepository: UserRepository): SearchUserService =
    SearchUserService.newInstance(userRepository)
}
