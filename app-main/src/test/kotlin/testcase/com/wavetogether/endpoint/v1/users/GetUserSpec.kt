package testcase.com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.v1.users.common.UserResponseImpl
import io.restassured.response.Response
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.com.wavetogether.endpoint.v1.V1EndpointTestBase
import testlib.com.wavetogether.endpoint.v1.ApiPathsHelper
import testlib.com.wavetogether.restassured.errorResponseEnvelope
import java.util.*

class GetUserSpec : V1EndpointTestBase() {
  @Nested
  @DisplayName("API call will fail when:")
  inner class FailWhen {
    @Test
    @DisplayName("key is empty(HTTP 404)")
    fun `HTTP 400 when key is empty`(testInfo: TestInfo) {
      // expect:
      requestFail(testInfo, ApiPathsHelper.USERS_KEY(""), HttpStatus.NOT_FOUND)
    }

    @Test
    @DisplayName("key of user is not found(HTTP 404)")
    fun `HTTP 404 when key of user is not found`(testInfo: TestInfo) {
      // given:
      val key = UUID.randomUUID()

      // expect:
      requestFail(testInfo, ApiPathsHelper.USERS_KEY(key), HttpStatus.NOT_FOUND)
    }
  }

  @Test
  @DisplayName("Can find a user whom corresponds to given key")
  fun `Can find a user whom corresponds to given key`(testInfo: TestInfo) {
    // given:
    val createdUser = createRandomUser(this)

    // then:
    val queriedUser = requestSuccess(testInfo, ApiPathsHelper.USERS_KEY(createdUser.key))

    // expect:
    assertThat(queriedUser, `is`(createdUser))
  }

  private fun requestFail(testInfo: TestInfo, endpoint: String, status: HttpStatus) =
    sendRequest(testInfo, endpoint, errorResponseEnvelope()).expectError(status)

  private fun requestSuccess(testInfo: TestInfo, endpoint: String) =
    sendRequest(testInfo, endpoint, userResponseSnippets())
      .expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)

  private fun sendRequest(testInfo: TestInfo, endpoint: String, responseFields: ResponseFieldsSnippet): Response {
    return jsonHttpGet(testInfo, endpoint, null, responseFields)
  }
}
