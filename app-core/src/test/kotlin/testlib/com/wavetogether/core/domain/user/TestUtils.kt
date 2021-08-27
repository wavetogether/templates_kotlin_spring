package testlib.com.wavetogether.core.domain.user

import com.github.javafaker.Faker
import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.impl.EditableUserVo
import java.util.*

fun randomUser(
  id: Long? = null,
  key: UUID? = null,
  name: String? = null
): User = EditableUserVo(
  id = id,
  key = key ?: UUID.randomUUID(),
  name = name ?: Faker.instance().funnyName().name()
)
