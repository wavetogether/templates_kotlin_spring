package testcase.com.wavetogether.endpoint.v1

import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.`is`
import org.springframework.http.HttpStatus
import testcase.com.wavetogether.endpoint.RestAssuredTestBase

class V1EndpointTestBase : RestAssuredTestBase() {
  fun <T> ValidatableResponse.expectSuccess(status: HttpStatus, klass: Class<T>): T {
    val genericResponse = this.asAbstractGenericResponse(status)
    val body = genericResponse.body

    return super.objectMapper.convertValue(body, klass)
  }

  fun ValidatableResponse.expectError(status: HttpStatus): GenericErrorResponse.Body {
    val genericResponse = this.asAbstractGenericResponse(status)

    if (genericResponse is GenericErrorResponse) {
      return genericResponse.body
    }

    throw AssertionError("Response is not an error: $genericResponse")
  }

  private fun ValidatableResponse.asAbstractGenericResponse(status: HttpStatus): AbstractGenericResponse<out Any> {
    val jsonStringResponse = this.assertThat().statusCode(`is`(status.value()))
      .extract().body().asString()
    val keyNotFound: (key: String) -> UnsupportedOperationException = {
      UnsupportedOperationException("No \"$it\" is found in: $jsonStringResponse")
    }

    val responseMap = super.objectMapper.readValue(jsonStringResponse, Map::class.java)
    val type = AbstractGenericResponse.Type.from(responseMap["type"]?.toString())
    val body = responseMap["body"] as? Map<*, *> ?: throw keyNotFound("body")

    return when (type) {
      AbstractGenericResponse.Type.OK -> AbstractGenericResponse.ok(body)
      AbstractGenericResponse.Type.ERROR -> {
        val message = body["message"] as? String ?: throw keyNotFound("message")
        val cause = body["cause"] as? String ?: throw keyNotFound("cause")

        /* return when */ AbstractGenericResponse.error(message, cause)
      }
    }
  }
}
