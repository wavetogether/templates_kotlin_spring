package com.wavetogether.endpoint._common.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wavetogether.lib.time.utcEpochSecond
import com.wavetogether.lib.time.utcNow

@JsonSerialize
abstract class AbstractGenericResponse<T>(
  @JsonProperty
  @JsonPropertyDescription(DESC_TYPE)
  val type: Type,

  @JsonProperty
  @JsonPropertyDescription(DESC_TIMESTAMP)
  val timestamp: Long = utcNow().utcEpochSecond()
) {
  @get:JsonProperty
  @get:JsonPropertyDescription(DESC_BODY)
  abstract val body: T

  companion object {
    const val DESC_TYPE = "Type of enclosing response. Either be 'OK' or 'ERROR'."
    const val DESC_TIMESTAMP = "UNIX epoch timestamp of request negotiation."
    const val DESC_BODY = "An actual payload of this response object."

    fun <T> ok(payload: T) = GenericOkResponse(payload)

    fun error(message: String, cause: String = "") = GenericErrorResponse(
      GenericErrorResponse.Body(message, cause)
    )
  }

  enum class Type(val value: String) {
    OK("ok"),
    ERROR("error");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
      return value
    }

    companion object {
      @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
      @JvmStatic
      fun from(value: String?): Type =
        values().firstOrNull { it.value == value } ?: throw IllegalArgumentException("Not a type string: $value")
    }
  }
}
