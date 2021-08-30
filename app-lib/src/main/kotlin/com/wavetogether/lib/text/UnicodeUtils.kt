package com.wavetogether.lib.text

import com.vdurmont.emoji.EmojiParser
import com.wavetogether.lib.util.toArray
import java.text.BreakIterator

@SuppressWarnings("ReturnCount")
fun String?.isNullOrUnicodeBlank(): Boolean {
  if (isNullOrBlank()) {
    return true
  }

  this.forEach {
    if (!UNICODE_BLANK_CHARS.contains(it)) {
      return false
    }
  }

  return true
}

fun String.unicodeGraphemeCount(): Int {
  var count = 0
  EmojiIterator.iterateOn(this) { ++count }
  return count
}

/*
 * Referenced from: https://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
 * Pretty display at: https://www.fileformat.info/info/unicode/category/Zs/list.htm
 *
 * Type notation to ensure this collection is immutable even though a reference leakage happens
 */
@Suppress("DuplicatedCode")
private val UNICODE_BLANK_CHARS: Set<Char> = setOf(
  '\u0009', // control-0009
  '\u000A', // control-000A
  '\u000B', // control-000B
  '\u000C', // control-000C
  '\u000D', // control-000D
  '\u001C', // FILE SEPARATOR
  '\u001D', // GROUP SEPARATOR
  '\u001E', // RECORD SEPARATOR
  '\u001F', // UNIT SEPARATOR
  '\u0020', // SPACE
  '\u0085', // control-0085
  '\u00A0', // NO-BREAK SPACE
  '\u1680', // OGHAM SPACE MARK
  '\u2000', // EN QUAD
  '\u2001', // EM QUAD
  '\u2002', // EN SPACE
  '\u2003', // EM SPACE
  '\u2004', // THREE-PER-EM SPACE
  '\u2005', // FOUR-PER-EM SPACE
  '\u2006', // SIX-PER-EM SPACE
  '\u2007', // FIGURE SPACE
  '\u2008', // PUNCTUATION SPACE
  '\u2009', // THIN SPACE
  '\u200A', // HAIR SPACE
  '\u202F', // NARROW NO-BREAK SPACE
  '\u205F', // MEDIUM MATHEMATICAL SPACE
  '\u3000'  // IDEOGRAPHIC SPACE
)

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
private object EmojiIterator : EmojiParser() {
  /**
   * [List of invisible Unicode characters](http://unicode.org/faq/unsup_char.html)
   *
   * See also: https://www.fileformat.info/info/unicode/category/Cf/list.htm
   */
  @Suppress("MagicNumber", "SpreadOperator")
  private val INVISIBLE_UNICODE_CHARS = setOf(
    0x00AD,                                         // Soft hyphen
    0x115F, 0x1160,                                 // Hangul Jamo fillers
    0x200B,                                         // Zero-width space
    0x200C, 0x200D,                                 // Cursive joiners
    0x200E, 0x200F, *(0x202A..0x202E).toArray(),    // Bidirectional format controls
    0x205F, *(0x2061..0x206F).toArray(),            // General punctuations
    *(0xFE00..0xFE0F).toArray(),                    // Variation selectors
    0x2060, 0xFEFF                                  // Word joiners
  )

  fun iterateOn(seq: String, callback: (String) -> Unit) {
    if (seq.isEmpty()) {
      return
    }

    val it = BreakIterator.getCharacterInstance().apply { setText(seq) }
    var idx = 0

    while (idx < seq.length) {
      val emojiEnd = getEmojiEndPos(seq.toCharArray(), idx)
      if (emojiEnd > -1) {
        callback(seq.substring(idx, emojiEnd))
        idx = emojiEnd
        continue
      }

      val codePoint = seq.codePointAt(idx)

      val oldIdx = idx
      idx = it.following(idx)

      if (!INVISIBLE_UNICODE_CHARS.contains(codePoint)) {
        callback(seq.substring(oldIdx, idx))
      }
    }
  }
}
