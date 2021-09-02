package testcase.com.wavetogether.lib.text

import com.wavetogether.lib.text.isNullOrUnicodeBlank
import com.wavetogether.lib.text.unicodeGraphemeCount
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import testlib.com.wavetogether.TestTags.TEST_SMALL
import java.util.stream.Stream

@Tag(TEST_SMALL)
class UnicodeUtilsTest {
  @ParameterizedTest(name = "\"{1}\" glyph count must be: {0}")
  @MethodSource("testUnicodeGraphemeCountArgs")
  fun `unicodeGraphemeCount for various inputs`(length: Int, charSeq: String) {
    Assertions.assertEquals(length, charSeq.unicodeGraphemeCount())
  }

  @Test
  fun `isUnicodeBlank yields expected result upon given input`() {
    val withInput: (String) -> Boolean = { it.isNullOrUnicodeBlank() }

    Assertions.assertEquals(true, withInput(""))
    Assertions.assertEquals(true, withInput("   "))
    Assertions.assertEquals(false, withInput(" a "))
    Assertions.assertEquals(false, withInput("\u3000 a \u3000"))
    Assertions.assertEquals(true, withInput("\u3000\u3000"))
  }

  companion object {
    @JvmStatic
    @Suppress("unused")
    fun testUnicodeGraphemeCountArgs(): Stream<Arguments> {
      return Stream.of(
        Arguments.of(0, ""),
        // Thai NFC case
        Arguments.of(28, "I 💚 Thai(บล็อกยูนิโคด) language"),
        // Emoji (7 characters for 1 glyph)
        Arguments.of(1, "🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
        // Chinese
        Arguments.of(14, "威爾士國旗看起來像： 🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
        // Japanese
        Arguments.of(10, "日本🇯🇵 大好き💚️!!"),
        // Korean NFD case
        Arguments.of(3, "\u1100\u1161\u1102\u1161\u1103\u1161")
      )
    }
  }
}
