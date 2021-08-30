package com.wavetogether.endpoint._common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class GenericOkResponse<T>(
  @JsonProperty
  @JsonPropertyDescription(DESC_BODY)
  override val body: T
) : AbstractGenericResponse<T>(Type.OK)
