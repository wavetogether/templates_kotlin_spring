package testlib.com.wavetogether.endpoint.v1

import com.wavetogether.endpoint.ApiPaths
import java.util.*

@Suppress("TestFunctionName")
object ApiPathsHelper {
  /**
   * @see [ApiPaths.USERS_KEY]
   */
  fun USERS_KEY(key: UUID): String = USERS_KEY(key.toString())

  /**
   * @see [ApiPaths.USERS_KEY]
   */
  fun USERS_KEY(key: String): String {
    return ApiPaths.USERS + "/$key"
  }
}
