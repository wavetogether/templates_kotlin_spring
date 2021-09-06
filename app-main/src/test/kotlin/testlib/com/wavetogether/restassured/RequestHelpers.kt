package testlib.com.wavetogether.restassured

import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.TestInfo
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import testcase.com.wavetogether.endpoint.RestAssuredTestBase

/**
 * Use this method for internal usage
 */
fun jsonHttpGet(testContext: RestAssuredTestBase, endpoint: String): ValidatableResponse =
  jsonHttpGet(testContext, null, endpoint)

/**
 * Use this method for automatic documentation
 */
fun jsonHttpGet(testContext: RestAssuredTestBase, testInfo: TestInfo?, endpoint: String): ValidatableResponse =
  newRequestSpec(testContext, testInfo)
    .contentType(ContentType.JSON)
    .`when`()
    .get(endpoint)
    .then()

/**
 * Use this method for internal usage
 */
fun jsonHttpPost(
  testContext: RestAssuredTestBase,
  endpoint: String,
  requestBody: Any? = null
): ValidatableResponse =
  jsonHttpPost(testContext, null, endpoint, requestBody)

/**
 * Use this method for automatic documentation
 */
fun jsonHttpPost(
  testContext: RestAssuredTestBase,
  testInfo: TestInfo?,
  endpoint: String,
  requestBody: Any? = null
): ValidatableResponse {
  return newRequestSpec(testContext, testInfo)
    .body(requestBody)
    .contentType(ContentType.JSON)
    .`when`()
    .post(endpoint)
    .then()
}

private fun newRequestSpec(testContext: RestAssuredTestBase, testInfo: TestInfo?): RequestSpecification {
  val documentId = if (testInfo == null) {
    ""
  } else {
    testInfo.testClass.map {
      it.toString().replaceFirst("class", "").trim()
    }.orElse("") + "$" + testInfo.displayName
  }

  var requestSpec = if (testContext.documentationSpec == null) {
    RestAssured.given()
  } else {
    RestAssured.given(testContext.documentationSpec)
  }

  requestSpec = requestSpec.log().all().port(testContext.port)

  if (documentId.isNotBlank()) {
    requestSpec = requestSpec.filter(document(documentId))
  }

  return requestSpec
    .config(
      RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> testContext.objectMapper }
      )
    )
}
