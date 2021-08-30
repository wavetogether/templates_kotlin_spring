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

fun mockPersistedUser(value: User): User.Persisted {
  return object : User.Persisted {
    override val id: Long = value.id ?: Faker.instance().random().nextLong()
    override val key: UUID = value.key
    override val name: String = value.name

    override fun edit(): User.Editable =
      throw UnsupportedOperationException("Cannot edit this mock object")
  }
}
