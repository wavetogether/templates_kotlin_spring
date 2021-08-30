package com.wavetogether.endpoint

object ApiPaths {
  /** Used by Spring default */
  const val ERROR = "/error"

  const val V_ALL = "/v*"
  const val V1 = "/v1"
  const val LATEST_VERSION = V1

  const val USERS = "$LATEST_VERSION/users"
}
