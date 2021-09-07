package testcase.com.wavetogether.endpoint.v1

import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.AbstractGenericResponse.Companion.FIELD_BODY
import com.wavetogether.endpoint._common.response.AbstractGenericResponse.Companion.FIELD_TYPE
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse.Companion.FIELD_CAUSE
import com.wavetogether.endpoint._common.response.GenericErrorResponse.Companion.FIELD_MESSAGE
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.TestInfo
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.com.wavetogether.endpoint.RestAssuredTestBase

class V1EndpointTestBase : RestAssuredTestBase() {
  fun <T> Response.expectSuccess(status: HttpStatus, klass: Class<T>): T {
    val genericResponse = this.then().asAbstractGenericResponse(status)
    val body = genericResponse.body

    return super.objectMapper.convertValue(body, klass)
  }

  fun Response.expectError(status: HttpStatus): GenericErrorResponse.Body {
    val genericResponse = this.then().asAbstractGenericResponse(status)

    if (genericResponse is GenericErrorResponse) {
      return genericResponse.body
    }

    throw AssertionError("Response is not an error: $genericResponse")
  }

  protected fun jsonHttpGet(
    testInfo: TestInfo,
    endpoint: String,
    reqFields: RequestFieldsSnippet?,
    respFields: ResponseFieldsSnippet?
  ) = testlib.com.wavetogether.restassured.jsonHttpGet(
    this,
    testInfo,
    endpoint,
    reqFields,
    respFields
  )

  protected fun jsonHttpPost(
    testInfo: TestInfo,
    endpoint: String,
    request: Any? = null,
    reqFields: RequestFieldsSnippet?,
    respFields: ResponseFieldsSnippet?
  ) = testlib.com.wavetogether.restassured.jsonHttpPost(
    this,
    testInfo,
    endpoint,
    request,
    reqFields,
    respFields
  )

  private fun ValidatableResponse.asAbstractGenericResponse(status: HttpStatus): AbstractGenericResponse<out Any> {
    val jsonStringResponse = this.assertThat().statusCode(`is`(status.value()))
      .extract().body().asString()
    val keyNotFound: (key: String) -> UnsupportedOperationException = {
      UnsupportedOperationException("No \"$it\" is found in: $jsonStringResponse")
    }

    val responseMap = super.objectMapper.readValue(jsonStringResponse, Map::class.java)
    val type = AbstractGenericResponse.Type.from(responseMap[FIELD_TYPE]?.toString())
    val body = responseMap[FIELD_BODY] as? Map<*, *> ?: throw keyNotFound(FIELD_BODY)

    return when (type) {
      AbstractGenericResponse.Type.OK -> AbstractGenericResponse.ok(body)
      AbstractGenericResponse.Type.ERROR -> {
        val message = body[FIELD_MESSAGE] as? String ?: throw keyNotFound(FIELD_MESSAGE)
        val cause = body[FIELD_CAUSE] as? String ?: throw keyNotFound(FIELD_CAUSE)

        /* return when */ AbstractGenericResponse.error(message, cause)
      }
    }
  }
}
