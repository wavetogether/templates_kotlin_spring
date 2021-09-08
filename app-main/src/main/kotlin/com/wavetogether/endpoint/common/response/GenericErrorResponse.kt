package com.wavetogether.endpoint.common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wavetogether.endpoint.common.response.GenericErrorResponse.Body

@JsonSerialize
data class GenericErrorResponse(
  @JsonProperty
  @JsonPropertyDescription(DESC_BODY)
  override val body: Body
) : AbstractGenericResponse<Body>(Type.ERROR) {
  data class Body(
    @JsonPropertyDescription(DESC_BODY_MESSAGE)
    @JsonProperty
    val message: String,

    @JsonPropertyDescription(DESC_BODY_CAUSE)
    @JsonProperty
    val cause: String
  )

  companion object {
    const val FIELD_MESSAGE = "message"
    const val FIELD_CAUSE = "cause"

    const val DESC_BODY_MESSAGE = "Detailed message of this error. Human readable hint text."
    const val DESC_BODY_CAUSE = "Cause of this error. Utilised for detecting reasons programmatically."
  }
}
