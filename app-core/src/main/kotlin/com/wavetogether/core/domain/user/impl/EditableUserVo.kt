package com.wavetogether.core.domain.user.impl

import com.wavetogether.core.domain.user.User
import java.util.*

internal class EditableUserVo(
  override var id: Long? = null,
  override val key: UUID,
  override var name: String
) : User.Editable {
  override fun edit(): User.Editable = EditableUserVo(
    id = this.id,
    key = this.key,
    name = this.name
  )

  override fun toString(): String {
    return """UserVo(
      | id=$id,
      | key=$key,
      | name='$name'
      |)""".trimMargin()
  }
}
