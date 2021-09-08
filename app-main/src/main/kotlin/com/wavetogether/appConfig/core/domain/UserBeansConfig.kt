package com.wavetogether.appConfig.core.domain

import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.infrastructure.domain.user.MemoryUserRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserBeansConfig {
  @Bean
  fun userRepository(): UserRepository {
    return MemoryUserRepositoryImpl()
  }
}
