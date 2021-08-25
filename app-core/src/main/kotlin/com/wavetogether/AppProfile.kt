package com.wavetogether

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
enum class AppProfile(val profileName: String) {
  LOCAL("local"),
  ALPHA("alpha"),
  BETA("beta"),
  RELEASE("release");

  companion object {
    fun from(profileName: String) = values().firstOrNull { it.profileName == profileName } ?: LOCAL
  }
}
