package testlib.com.wavetogether.restassured

import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import testcase.com.wavetogether.endpoint.RestAssuredTestBase

fun jsonHttpGet(testContext: RestAssuredTestBase, endpoint: String): ValidatableResponse {
  return newRequestSpec(testContext)
    .contentType(ContentType.JSON)
    .`when`()
    .get(endpoint)
    .then()
}

fun jsonHttpPost(testContext: RestAssuredTestBase, endpoint: String, requestBody: Any? = null): ValidatableResponse {
  return newRequestSpec(testContext)
    .body(requestBody)
    .contentType(ContentType.JSON)
    .`when`()
    .post(endpoint)
    .then()
}

private fun newRequestSpec(testContext: RestAssuredTestBase): RequestSpecification {
  val requestSpec = if (testContext.documentationSpec == null) {
    RestAssured.given()
  } else {
    RestAssured.given(testContext.documentationSpec)
  }

  return requestSpec.log().all()
    .port(testContext.port)
    .config(
      RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> testContext.objectMapper }
      )
    )
}
