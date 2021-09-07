package testcase.com.wavetogether.endpoint.v1.users

import com.github.javafaker.Faker
import com.wavetogether.endpoint.ApiPaths
import com.wavetogether.endpoint.v1.users._common.UserResponseImpl
import com.wavetogether.endpoint.v1.users.createUser.CreateUserRequestImpl
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.com.wavetogether.endpoint.v1.V1EndpointTestBase
import testlib.com.wavetogether.restassured.genericResponseEnvelope
import testlib.com.wavetogether.restassured.jsonHttpPost

fun CreateUserRequestImpl.Companion.createRandom(name: String? = null): CreateUserRequestImpl {
  return CreateUserRequestImpl(
    name = name ?: Faker.instance().funnyName().name()
  )
}

fun createRandomUser(
  testContext: V1EndpointTestBase,
  request: CreateUserRequestImpl? = null
): UserResponseImpl = with(testContext) {
  val requestBody = request ?: CreateUserRequestImpl.createRandom()

  return@with jsonHttpPost(testContext, ApiPaths.USERS, requestBody)
    .expectSuccess(HttpStatus.OK, UserResponseImpl::class.java)
}

fun userResponseSnippets(): ResponseFieldsSnippet {
  val userResponseFields = listOf(
    fieldWithPath("body.key")
      .type(JsonFieldType.STRING)
      .description(UserResponseImpl.DESC_KEY),
    fieldWithPath("body.name")
      .type(JsonFieldType.STRING)
      .description(UserResponseImpl.DESC_NAME)
  )

  return responseFields(genericResponseEnvelope() + userResponseFields)
}
