/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.wavetogether.lib.annotation

/**
 * Annotates a programme element which widened its visibility
 * other than necessary, is only for use in test codes.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2018
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class VisibleForTesting
