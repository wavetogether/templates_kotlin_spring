package testcase.com.wavetogether.endpoint.v1.users

import com.github.javafaker.Faker
import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.endpoint.v1.users.createUser.CreateUserRequestImpl
import org.springframework.http.HttpStatus
import testcase.com.wavetogether.endpoint.v1.V1EndpointRestAssuredTestBase
import testlib.com.wavetogether.restassured.jsonHttpPost

fun CreateUserRequestImpl.Companion.createRandom(name: String? = null): CreateUserRequestImpl {
  return CreateUserRequestImpl(
    name = name ?: Faker.instance().funnyName().name()
  )
}

fun createRandomUser(
  testContext: V1EndpointRestAssuredTestBase,
  request: CreateUserRequestImpl? = null
): UserResponseImpl = with(testContext) {
  val requestBody = request ?: CreateUserRequestImpl.createRandom()

  return@with jsonHttpPost(ApiPaths.USERS, requestBody).expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)
}
