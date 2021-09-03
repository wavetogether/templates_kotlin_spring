package testcase.com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import testcase.com.wavetogether.endpoint.v1.V1EndpointTestBase
import testlib.com.wavetogether.endpoint.v1.ApiPathsHelper
import testlib.com.wavetogether.restassured.jsonHttpGet
import java.util.*

class GetUserSpec : V1EndpointTestBase() {
  @Nested
  @DisplayName("GET /users will fail when:")
  inner class FailWhen {
    @Test
    @DisplayName("key is empty(HTTP 404)")
    fun `HTTP 400 when key is empty`() {
      // expect:
      jsonHttpGet(this@GetUserSpec, ApiPathsHelper.USERS_KEY("")).expectError(HttpStatus.NOT_FOUND)
    }

    @Test
    @DisplayName("key of user is not found(HTTP 404)")
    fun `HTTP 404 when key of user is not found`() {
      // given:
      val key = UUID.randomUUID()

      // expect:
      jsonHttpGet(this@GetUserSpec, ApiPathsHelper.USERS_KEY(key)).expectError(HttpStatus.NOT_FOUND)
    }
  }

  @Test
  @DisplayName("Can find a user whom corresponds to given key")
  fun `Can find a user whom corresponds to given key`() {
    // given:
    val createdUser = createRandomUser(this)

    // then:
    val queriedUser = jsonHttpGet(this@GetUserSpec, ApiPathsHelper.USERS_KEY(createdUser.key))
      .expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)

    // expect:
    assertThat(queriedUser, `is`(createdUser))
  }
}
