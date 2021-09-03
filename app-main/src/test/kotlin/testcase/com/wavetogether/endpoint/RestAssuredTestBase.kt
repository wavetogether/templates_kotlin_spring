package testcase.com.wavetogether.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wavetogether.Application
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import testlib.com.wavetogether.TestTags
import javax.inject.Inject

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = [Application::class]
)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@Tag(TestTags.TEST_LARGE)
class RestAssuredTestBase {
  val documentationSpec: RequestSpecification?
    get() = if (this::_documentationSpec.isInitialized) {
      this._documentationSpec
    } else {
      null
    }

  val objectMapper: ObjectMapper
    get() = if (this::defaultObjectMapper.isInitialized) {
      this.defaultObjectMapper
    } else {
      throw IllegalStateException("No ObjectMapper instance is injected during test lifecycle.")
    }

  val port: Int
    get() = this._port

  @Inject
  private lateinit var defaultObjectMapper: ObjectMapper

  @LocalServerPort
  private var _port: Int = 0

  private lateinit var _documentationSpec: RequestSpecification

  @BeforeEach
  fun setup(restDocumentation: RestDocumentationContextProvider) {
    this._documentationSpec = RequestSpecBuilder()
      .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
      .build()
  }
}
