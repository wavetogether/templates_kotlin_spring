package com.wavetogether.core.domain.user

import java.util.*

interface User {
  /**
   * Field to identify a certain user in domain object pool. Better not to expose outside world.
   * Instances that holds a `null` id means that it maybe not under in a persistent state.
   */
  val id: Long?

  /**
   * Field to identify a certain user in domain object pool, used by outside world.
   */
  val key: UUID

  val name: String

  fun edit(): Editable

  interface Editable : User {
    override var id: Long?

    override var name: String
  }

  interface Persisted : User {
    override val id: Long
  }
}
