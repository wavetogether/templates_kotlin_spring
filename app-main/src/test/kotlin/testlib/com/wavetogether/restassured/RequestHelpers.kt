package testlib.com.wavetogether.restassured

import org.springframework.restdocs.snippet.Snippet
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.TestInfo
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import testcase.com.wavetogether.endpoint.RestAssuredTestBase

const val DEFAULT_HOST = "localhost"
const val DEFAULT_PORT = 8080

/**
 * Use this method for internal usage
 */
fun jsonHttpGet(
  testContext: RestAssuredTestBase,
  endpoint: String,
  reqFields: RequestFieldsSnippet? = null,
  respFields: ResponseFieldsSnippet? = null
): Response =
  jsonHttpGet(testContext, null, endpoint, reqFields, respFields)

/**
 * Use this method for automatic documentation
 */
fun jsonHttpGet(
  testContext: RestAssuredTestBase,
  testInfo: TestInfo?,
  endpoint: String,
  reqFields: RequestFieldsSnippet? = null,
  respFields: ResponseFieldsSnippet? = null
): Response =
  newRequestSpec(testContext, testInfo, reqFields, respFields)
    .contentType(ContentType.JSON)
    .`when`()
    .get(endpoint)

/**
 * Use this method for internal usage
 */
fun jsonHttpPost(
  testContext: RestAssuredTestBase,
  endpoint: String,
  requestBody: Any? = null,
  reqFields: RequestFieldsSnippet? = null,
  respFields: ResponseFieldsSnippet? = null
): Response =
  jsonHttpPost(testContext, null, endpoint, requestBody, reqFields, respFields)

/**
 * Use this method for automatic documentation
 */
fun jsonHttpPost(
  testContext: RestAssuredTestBase,
  testInfo: TestInfo?,
  endpoint: String,
  requestBody: Any? = null,
  reqFields: RequestFieldsSnippet? = null,
  respFields: ResponseFieldsSnippet? = null
): Response {
  return newRequestSpec(testContext, testInfo, reqFields, respFields)
    .body(requestBody)
    .contentType(ContentType.JSON)
    .`when`()
    .post(endpoint)
}

private fun newRequestSpec(
  testContext: RestAssuredTestBase,
  testInfo: TestInfo?,
  reqFields: RequestFieldsSnippet? = null,
  respFields: ResponseFieldsSnippet? = null
): RequestSpecification {
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

  val snippets: Array<Snippet> = ArrayList<Snippet>().run {
    reqFields?.let { add(it) }
    respFields?.let { add(it) }

    return@run toArray(arrayOfNulls(this.size))
  }

  if (documentId.isNotBlank()) {
    requestSpec = requestSpec.filter(
      document(
        documentId,
        preprocessRequest(modifyUris().host(DEFAULT_HOST).port(DEFAULT_PORT), prettyPrint()),
        preprocessResponse(prettyPrint()),
        *snippets
      )
    )
  }

  return requestSpec
    .config(
      RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> testContext.objectMapper }
      )
    )
}
