package testcase.com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.endpoint.v1.users.createUser.CreateUserRequestImpl
import io.restassured.response.Response
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.com.wavetogether.endpoint.v1.V1EndpointTestBase
import testlib.com.wavetogether.lib.text.randomAlphaNumeric
import testlib.com.wavetogether.restassured.errorResponseEnvelope

class CreateUserSpec : V1EndpointTestBase() {
  @Nested
  @DisplayName("API call will fail when(HTTP 400):")
  inner class FailIfArgumentsAreWrong {
    @Test
    @DisplayName("`name` is shorter than ${CreateUserRequestImpl.NAME_LENGTH_MIN} characters")
    fun `name is too short`(testInfo: TestInfo) {
      // given:
      val min = CreateUserRequestImpl.NAME_LENGTH_MIN - 1
      val request = CreateUserRequestImpl.createRandom(name = randomAlphaNumeric(min, min))

      // expect:
      expectBadRequestException(testInfo, request)
    }

    @Test
    @DisplayName("`name` is longer than ${CreateUserRequestImpl.NAME_LENGTH_MAX} characters")
    fun `name is too long`(testInfo: TestInfo) {
      // given:
      val max = CreateUserRequestImpl.NAME_LENGTH_MAX + 1
      val request = CreateUserRequestImpl.createRandom(name = randomAlphaNumeric(max))

      // expect:
      expectBadRequestException(testInfo, request)
    }

    private fun expectBadRequestException(testInfo: TestInfo, requestBody: CreateUserRequestImpl) {
      // then:
      val response = requestFail(testInfo, ApiPaths.USERS, requestBody, HttpStatus.BAD_REQUEST)

      // expect:
      assertThat(response.cause, `is`("BadRequestException"))
    }
  }

  @Test
  @DisplayName("Creating user will success with valid request")
  fun `creating user will success with valid request`(testInfo: TestInfo) {
    // given:
    val name = randomAlphaNumeric(CreateUserRequestImpl.NAME_LENGTH_MIN, CreateUserRequestImpl.NAME_LENGTH_MAX)
    val request = CreateUserRequestImpl.createRandom(name = name)

    // then:
    val response = requestSuccess(testInfo, ApiPaths.USERS, request)

    // expect:
    assertThat(response.name, `is`(name))
  }

  private fun requestFail(testInfo: TestInfo, endpoint: String, request: Any?, status: HttpStatus) =
    sendRequest(testInfo, endpoint, request, createUserRequestSnippets(), errorResponseEnvelope())
      .expectError(status)

  private fun requestSuccess(testInfo: TestInfo, endpoint: String, request: Any?) =
    sendRequest(testInfo, endpoint, request, createUserRequestSnippets(), userResponseSnippets())
      .expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)

  private fun sendRequest(
    testInfo: TestInfo,
    endpoint: String,
    request: Any?,
    requestFields: RequestFieldsSnippet,
    responseFields: ResponseFieldsSnippet
  ): Response {
    return jsonHttpPost(testInfo, endpoint, request, requestFields, responseFields)
  }
}

private fun createUserRequestSnippets(): RequestFieldsSnippet {
  return requestFields(
    fieldWithPath("name")
      .type(JsonFieldType.STRING)
      .description(CreateUserRequestImpl.DESC_NAME)
  )
}
