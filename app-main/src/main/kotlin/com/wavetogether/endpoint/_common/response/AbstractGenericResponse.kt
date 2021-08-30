package com.wavetogether.endpoint._common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.wavetogether.lib.time.utcEpochSecond
import com.wavetogether.lib.time.utcNow

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

  enum class Type {
    OK,
    ERROR
  }
}
