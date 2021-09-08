package com.wavetogether.endpoint.common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wavetogether.endpoint.common.response.PageableResponse.Body

@JsonSerialize
data class PageableResponse<K, T>(
  @JsonProperty
  @JsonPropertyDescription(DESC_BODY)
  override val body: Body<K, T>
) : AbstractGenericResponse<Body<K, T>>(Type.OK) {
  data class Body<K, T>(
    @JsonProperty
    @JsonPropertyDescription(DESC_FROM_OFFSET)
    val fromOffset: K,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_OFFSET)
    val lastOffset: K,

    @JsonProperty
    @JsonPropertyDescription(DESC_SIZE)
    val size: Int,

    @JsonProperty
    @JsonPropertyDescription(DESC_TOTAL_COUNT)
    val totalCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_DATA)
    val data: List<T>
  )

  companion object {
    const val DESC_FROM_OFFSET = "Initial offset of this data list."
    const val DESC_LAST_OFFSET = "Last offset of this data list."
    const val DESC_SIZE = "Requested size of data list. Note that this value may differ to size of `data` if " +
        "there are fewer data than requested size."
    const val DESC_TOTAL_COUNT = "Total count of available data."
    const val DESC_DATA = "Actual data of windowed request."

    fun <K, T> create(
      fromOffset: K,
      lastOffset: K,
      size: Int,
      totalCount: Long,
      data: Collection<T>
    ) = PageableResponse(
      body = Body(
        fromOffset = fromOffset,
        lastOffset = lastOffset,
        size = size,
        totalCount = totalCount,
        data = data.toList()
      )
    )
  }
}
