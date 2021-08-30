package com.wavetogether.lib.util

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
fun ClosedRange<Int>.toArray() = Array((endInclusive - start) + 1) { i -> start + i }
