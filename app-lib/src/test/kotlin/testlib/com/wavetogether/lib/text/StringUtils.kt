package testlib.com.wavetogether.lib.text

import com.github.javafaker.Faker

/**
 * Creates an alphanumeric string with a length between [minLength] and [maxLength].
 *
 * @param minLength 2 if <code>null</code>
 * @param maxLength 32 if <code>null</code>. Must be larger than [minLength].
 */
fun randomAlphaNumeric(minLength: Int? = null, maxLength: Int? = null): String {
  val faker = Faker.instance()

  val actualMinLength = if (minLength == null) {
    2
  } else {
    if (minLength < 1) {
      throw IllegalArgumentException("minLength: $minLength < 1")
    } else {
      minLength
    }
  }

  val actualMaxLength = if (maxLength == null) {
    32
  } else {
    if (maxLength < actualMinLength) {
      throw IllegalArgumentException("maxLength: $minLength < $actualMinLength")
    } else {
      maxLength
    }
  }

  val actualLength = faker.random().nextInt(actualMinLength, actualMaxLength)
  return faker.letterify("?".repeat(actualLength))
}
