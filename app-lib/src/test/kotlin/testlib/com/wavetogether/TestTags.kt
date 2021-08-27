package testlib.com.wavetogether

/**
 * Collection of string literals correspond to gradle tasks that filters tests by tags.
 * These literals must match to `TAG_TEST_SMALL`, `TAG_TEST_MEDIUM` and `TAG_TEST_LARGE` which are defined
 * in Gradle build script located in PROJECT_ROOT/gradle/script/testing.gradle.
 */
object TestTags {
  /**
   * Expected execution time per test cases: < 200ms
   *
   * Small tests should be run very frequently during development stage.
   *
   * All small tests must be scoped to instrument single method and/or class.
   * Developers should write these tests run in an isolated environment and use mocks for external dependencies.
   *
   * These type of tests are also known as 'Unit test' and must be written in manner of white box testing.
   */
  const val TEST_SMALL = "smallTest"

  /**
   * Expected execution time per test cases: < 5000ms
   *
   * Medium tests should be run during every code check-in/commit/sync/etc., stage.
   *
   * All medium tests must be scoped to instrument single method and/or class.
   * Resource access via external dependencies such as network/database/filesystem/etc.,
   * is/are permitted during this stage, if those components are under control during test.
   *
   * These type of tests are also known as 'Integration test' and must be written in manner of white box testing.
   */
  const val TEST_MEDIUM = "mediumTest"

  /**
   * Expected execution time per test cases: > 5000ms
   *
   * Large tests should be run during every code integration stage. These tests are generally run on
   * Continuous Integration server.
   *
   * All large tests should be focused on testing integration of all application components.
   * All type of resource access via external dependencies, should not be mocked and fully participated in the system
   * as much as possible.
   *
   * These type of tests are also known as 'End to end test' and must be written in manner of black box testing.
   */
  const val TEST_LARGE = "largeTest"
}
