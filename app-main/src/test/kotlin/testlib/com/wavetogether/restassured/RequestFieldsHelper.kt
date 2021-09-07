package testlib.com.wavetogether.restassured

import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.ResponseFieldsSnippet

fun genericResponseEnvelope() = listOf(
  fieldWithPath("type")
    .type(JsonFieldType.STRING)
    .description(AbstractGenericResponse.DESC_TYPE),
  fieldWithPath("timestamp")
    .type(JsonFieldType.NUMBER)
    .description(AbstractGenericResponse.DESC_TIMESTAMP),
  fieldWithPath("body")
    .type(JsonFieldType.OBJECT)
    .description(AbstractGenericResponse.DESC_BODY),
)

fun errorResponseEnvelope(): ResponseFieldsSnippet {
  val errorFields = listOf(
    fieldWithPath("body.message")
      .type(JsonFieldType.STRING)
      .description(GenericErrorResponse.DESC_BODY_MESSAGE),
    fieldWithPath("body.cause")
      .type(JsonFieldType.STRING)
      .description(GenericErrorResponse.DESC_BODY_CAUSE)
  )

  return responseFields(genericResponseEnvelope() + errorFields)
}
