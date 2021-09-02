package com.wavetogether.endpoint.v1.users.createUser

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.wavetogether.validation.UnicodeCharsLength

@JsonDeserialize
data class CreateUserRequestImpl(
  @get:UnicodeCharsLength(
    min = NAME_LENGTH_MIN,
    max = NAME_LENGTH_MAX,
    message = "`name` must between $NAME_LENGTH_MIN and $NAME_LENGTH_MAX characters."
  )
  @JsonProperty
  @JsonPropertyDescription(DESC_NAME)
  val name: String
) {
  companion object {
    const val NAME_LENGTH_MIN = 2
    const val NAME_LENGTH_MAX = 8

    const val DESC_NAME = "A name of user."
  }
}
