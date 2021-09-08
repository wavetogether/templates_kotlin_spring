package testcase.com.wavetogether.infrastruture.domain.user

import com.github.javafaker.Faker
import com.wavetogether.infrastructure.domain.user.MemoryUserRepositoryImpl
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.*
import testlib.com.wavetogether.TestTags.TEST_MEDIUM
import testlib.com.wavetogether.core.domain.user.randomUser
import java.util.*

@Tag(TEST_MEDIUM)
class MemoryUserRepositoryImplTest {
  private val faker = Faker.instance()
  private lateinit var sut: MemoryUserRepositoryImpl

  @BeforeEach
  fun setup() {
    this.sut = MemoryUserRepositoryImpl()
  }

  @Test
  @DisplayName("Id is automatically increased per consecutive put operations.")
  fun `id is automatically increased per consecutive put operations`() {
    // given:
    val firstUser = sut.save(randomUser())
    val secondUser = sut.save(randomUser())

    // expect:
    assertThat(firstUser.id, `is`(1L))
    assertThat(secondUser.id, `is`(2L))
  }

  @Test
  @DisplayName("save() call upon persisted object does not increase id value.")
  fun `save() call on persisted object does not increase id value`() {
    // given:
    val savedUser = sut.save(randomUser())

    // then:
    assertThat(savedUser.id, `is`(1L))

    // and:
    val updatedUser = sut.save(savedUser)

    // expect:
    assertThat(savedUser.id, `is`(updatedUser.id))
    assertThat(savedUser == updatedUser, `is`(true))

    // and: "Number of total records must not be increased"
    assertThat(sut.count(), `is`(1))
  }

  @Nested
  @DisplayName("find() method must return:")
  inner class FindMethodMustReturn {
    @Nested
    @DisplayName("null if there is no corresponding:")
    inner class NullIfNoRecord {
      @Test
      @DisplayName("id of user")
      fun `id of user`() {
        // given:
        val randomId = faker.random().nextLong()

        // expect:
        assertThat(sut.findById(randomId), `is`(nullValue()))
      }

      @Test
      @DisplayName("key of user")
      fun `key of user`() {
        // given:
        val randomKey = UUID.randomUUID()

        // expect:
        assertThat(sut.findByKey(randomKey), `is`(nullValue()))
      }
    }

    @Nested
    @DisplayName("exact user is there is corresponding:")
    inner class ExactUserIfRecord {
      @Test
      @DisplayName("id of user")
      fun `id of user`() {
        // given:
        val savedUser = sut.save(randomUser())

        // then:
        val id = savedUser.id

        // expect:
        assertThat(sut.findById(id), `is`(savedUser))
      }

      @Test
      @DisplayName("key of user")
      fun `key of user`() {
        // given:
        val savedUser = sut.save(randomUser())

        // expect:
        assertThat(sut.findByKey(savedUser.key), `is`(savedUser))
      }
    }
  }

  @Nested
  @DisplayName("delete() method must return:")
  inner class DeleteMethodMustReturn {
    @Nested
    @DisplayName("0 if there is no corresponding:")
    inner class ZeroIfNoRecord {
      private val expectedDeletionCount = 0

      @Test
      @DisplayName("id of user")
      fun `id of user`() {
        // given:
        val randomId = faker.random().nextLong()

        // expect:
        assertThat(sut.deleteById(randomId), `is`(expectedDeletionCount))
      }

      @Test
      @DisplayName("key of user")
      fun `key of user`() {
        // given:
        val randomKey = UUID.randomUUID()

        // expect:
        assertThat(sut.deleteByKey(randomKey), `is`(expectedDeletionCount))
      }
    }

    @Nested
    @DisplayName("1 if there is corresponding:")
    inner class CountIfRecord {
      private val expectedDeletionCount = 1

      @Test
      @DisplayName("id of user")
      fun `id of user`() {
        // given:
        val randomId = sut.save(randomUser()).id

        // expect:
        assertThat(sut.deleteById(randomId), `is`(expectedDeletionCount))

        // and:
        assertThat(sut.count(), `is`(0))
      }

      @Test
      @DisplayName("key of user")
      fun `key of user`() {
        // given:
        val randomKey = sut.save(randomUser()).key

        // expect:
        assertThat(sut.deleteByKey(randomKey), `is`(expectedDeletionCount))

        // and:
        assertThat(sut.count(), `is`(0))
      }
    }
  }
}
