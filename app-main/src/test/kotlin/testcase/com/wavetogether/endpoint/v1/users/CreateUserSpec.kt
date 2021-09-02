package testcase.com.wavetogether.endpoint.v1.users

import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.endpoint.v1.users.createUser.CreateUserRequestImpl
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import testcase.com.wavetogether.endpoint.v1.V1EndpointRestAssuredTestBase
import testlib.com.wavetogether.lib.text.randomAlphaNumeric
import testlib.com.wavetogether.restassured.jsonHttpPost

class CreateUserSpec : V1EndpointRestAssuredTestBase() {
  @Nested
  @DisplayName("POST /users will fail when(HTTP 400):")
  inner class FailIfArgumentsAreWrong {
    @Test
    @DisplayName("`name` is shorter than ${CreateUserRequestImpl.NAME_LENGTH_MIN} characters")
    fun `name is too short`() {
      // given:
      val request = CreateUserRequestImpl.createRandom(name = randomAlphaNumeric(1, 1))

      // expect:
      expectBadRequestException(request)
    }

    @Test
    @DisplayName("`name` is longer than ${CreateUserRequestImpl.NAME_LENGTH_MAX} characters")
    fun `name is too long`() {
      // given:
      val request = CreateUserRequestImpl.createRandom(name = randomAlphaNumeric(9))

      // expect:
      expectBadRequestException(request)
    }

    private fun expectBadRequestException(requestBody: CreateUserRequestImpl) {
      // then:
      val response = jsonHttpPost(ApiPaths.USERS, requestBody).expectError(HttpStatus.BAD_REQUEST)

      // expect:
      assertThat(response.cause, `is`("BadRequestException"))
    }
  }

  @Test
  @DisplayName("POST /user will success with valid request")
  fun `will success with valid request`() {
    // given:
    val name = randomAlphaNumeric(CreateUserRequestImpl.NAME_LENGTH_MIN, CreateUserRequestImpl.NAME_LENGTH_MAX)
    val request = CreateUserRequestImpl.createRandom(
      name = name
    )

    // then:
    val response = jsonHttpPost(ApiPaths.USERS, request).expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)

    // expect:
    assertThat(response.name, `is`(name))
  }
}
