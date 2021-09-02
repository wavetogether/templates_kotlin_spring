package testlib.com.wavetogether.restassured

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse

fun jsonHttpGet(endpoint: String): ValidatableResponse {
  return RestAssured.given().log().all()
    .contentType(ContentType.JSON)
    .`when`()
    .get(endpoint)
    .then()
}

fun jsonHttpPost(endpoint: String, requestBody: Any? = null): ValidatableResponse {
  return RestAssured.given().log().all()
    .body(requestBody)
    .contentType(ContentType.JSON)
    .`when`()
    .post(endpoint)
    .then()
}
