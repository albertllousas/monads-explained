package monads.either

import monads.either.Either.Left
import monads.either.Either.Right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EitherTest {

    @Nested
    inner class MapTests {

        @Test
        fun `should apply a fn when either is right`() {
            assertThat(Right(1).map { it + 1 }).isEqualTo(Right(2))
        }

        @Test
        fun `should not apply a fn when either is left`() {
            val either: Either<String, Int> = Left("Some Error")
            assertThat(either.map { it + 1 }).isEqualTo(Left("Some Error"))
        }
    }

    @Nested
    inner class FlatMapTests {

        @Test
        fun `should apply a fn when either is right`() {
            val justInc = { n:Int -> Right(n + 1) }
            assertThat(Right(1).flatMap(justInc)).isEqualTo(Right(2))
        }

        @Test
        fun `should not apply a fn when either is left`() {
            val justInc = { n:Int -> Right(n + 1) }
            val either: Either<String, Int> = Left("Some Error")
            assertThat(either.flatMap(justInc)).isEqualTo(Left("Some Error"))
        }
    }
}
