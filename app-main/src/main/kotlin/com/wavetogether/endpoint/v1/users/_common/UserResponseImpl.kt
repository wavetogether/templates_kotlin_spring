package com.wavetogether.endpoint.v1.users._common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wavetogether.core.domain.user.User
import java.util.*

@JsonSerialize
data class UserResponseImpl(
  @JsonProperty
  @JsonPropertyDescription(DESC_KEY)
  val key: UUID,

  @JsonProperty
  @JsonPropertyDescription(DESC_NAME)
  val name: String,
) {
  companion object {
    const val DESC_KEY = "An unique value to identify this user."
    const val DESC_NAME = "A name of user."

    fun from(user: User) = with(user) {
      return@with UserResponseImpl(
        key = key,
        name = name
      )
    }
  }
}
