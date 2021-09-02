package testcase.com.wavetogether.endpoint.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.wavetogether.Application
import com.wavetogether.endpoint._common.response.AbstractGenericResponse
import com.wavetogether.endpoint._common.response.GenericErrorResponse
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import testlib.com.wavetogether.TestTags.TEST_LARGE
import javax.inject.Inject

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = [Application::class]
)
@Tag(TEST_LARGE)
class V1EndpointRestAssuredTestBase {
  @Inject
  private lateinit var defaultObjectMapper: ObjectMapper

  @LocalServerPort
  private var port: Int = 0

  @BeforeEach
  fun setup() {
    RestAssured.port = port
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> defaultObjectMapper }
    )
  }

  fun <T> ValidatableResponse.expectSuccess(status: HttpStatus, klass: Class<T>): T {
    val objMapper = this@V1EndpointRestAssuredTestBase.defaultObjectMapper

    val genericResponse = this.asAbstractGenericResponse(status)
    val body = genericResponse.body

    return objMapper.convertValue(body, klass)
  }

  fun ValidatableResponse.expectError(status: HttpStatus): GenericErrorResponse.Body {
    val genericResponse = this.asAbstractGenericResponse(status)

    if (genericResponse is GenericErrorResponse) {
      return genericResponse.body
    }

    throw AssertionError("Response is not an error: $genericResponse")
  }

  private fun ValidatableResponse.asAbstractGenericResponse(status: HttpStatus): AbstractGenericResponse<out Any> {
    val objMapper = this@V1EndpointRestAssuredTestBase.defaultObjectMapper

    val jsonStringResponse = this.assertThat().statusCode(`is`(status.value()))
      .extract().body().asString()
    val keyNotFound: (key: String) -> UnsupportedOperationException = {
      UnsupportedOperationException("No \"$it\" is found in: $jsonStringResponse")
    }

    val responseMap = objMapper.readValue(jsonStringResponse, Map::class.java)
    val type = AbstractGenericResponse.Type.from(responseMap["type"]?.toString())
    val body = responseMap["body"] as? Map<*, *> ?: throw keyNotFound("body")

    return when (type) {
      AbstractGenericResponse.Type.OK -> AbstractGenericResponse.ok(body)
      AbstractGenericResponse.Type.ERROR -> {
        val message = body["message"] as? String ?: throw keyNotFound("message")
        val cause = body["cause"] as? String ?: throw keyNotFound("cause")

        /* return when */ AbstractGenericResponse.error(message, cause)
      }
    }
  }
}
