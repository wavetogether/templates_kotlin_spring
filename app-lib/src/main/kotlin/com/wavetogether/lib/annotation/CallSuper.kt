/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.wavetogether.lib.annotation

/**
 * Annotates that any overriding methods should invoke this method(of superclass) as well.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2018
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CallSuper
