package com.wavetogether

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
  Application().start(args)
}

@SpringBootApplication
class Application {
  private val profile = BuildConfig.currentProfile

  // Loads application.yml first, and overrides settings, declared if any, in application-<BUILD_PHASE>.yml.
  private val configurationNames = arrayOf("application", "application-" + profile.profileName)

  fun start(args: Array<String>) {
    // This logic is called only once.
    @Suppress("SpreadOperator")
    SpringApplicationBuilder(Application::class.java)
      .properties("spring.config.name:" + configurationNames.joinToString { it })
      .build()
      .run(*args)
  }
}
