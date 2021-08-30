package testcase.com.wavetogether.core.service.user

import com.github.javafaker.Faker
import com.wavetogether.core.domain.user.User
import com.wavetogether.core.domain.user.UserRepository
import com.wavetogether.core.service.user.CreateUserService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import testlib.com.wavetogether.TestTags.TEST_SMALL
import testlib.com.wavetogether.core.domain.user.mockPersistedUser

@Tag(TEST_SMALL)
class CreateUserServiceTest {
  private val faker = Faker.instance()

  private lateinit var userRepository: UserRepository
  private lateinit var sut: CreateUserService

  @BeforeEach
  fun setup() {
    this.userRepository = mock()
    this.sut = CreateUserService.newInstance(userRepository)

    // Let UserRepository#save acts transitively returning given object
    `when`(userRepository.save(any())).thenAnswer { mockPersistedUser(it.getArgument(0)) }
  }

  @Test
  @DisplayName("A new user instance is created after createUser is called.")
  fun `A new user instance is created after createUser is called`() {
    // given:
    val name = faker.funnyName().name()

    // then:
    val createdUser = sut.createUser(name)

    // expect:
    assertThat(createdUser.name, `is`(name))
  }

  @Test
  @DisplayName("A new user instance is saved upon createUser invocation.")
  fun `A new user instance is saved upon createUser invocation`() {
    // when:
    val createdUser = sut.createUser(faker.funnyName().name())

    // then:
    val userCaptor: KArgumentCaptor<User> = argumentCaptor()
    verify(userRepository, times(1)).save(userCaptor.capture())
    val savedUser = userCaptor.firstValue

    // expect:
    assertThat(savedUser.name, `is`(createdUser.name))
  }
}
