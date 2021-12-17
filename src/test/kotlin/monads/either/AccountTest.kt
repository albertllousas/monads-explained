package monads.either

import monads.either.AccountError.NegativeAmount
import monads.either.AccountError.NotEnoughFunds
import monads.either.Either.Left
import monads.either.Either.Right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class AccountTest {

    @Nested
    inner class CreateAnAccount {

        @Test
        fun `should create an account`() {
            val account = Account.create(100.toBigDecimal())
            assertThat(account).isEqualTo(Right(buildAccount(100.toBigDecimal())))
        }

        @Test
        fun `should fail creating an account with a negative amount`() {
            assertThat(Account.create((-100).toBigDecimal())).isEqualTo(Left(NegativeAmount))
        }
    }

    @Nested
    inner class Deposit {
        @Test
        fun `should deposit money to an account`() {
            val account = buildAccount(100.toBigDecimal())

            val updatedAccount = account.deposit(100.toBigDecimal())

            assertThat(updatedAccount).isEqualTo(Right(buildAccount(200.toBigDecimal())))
        }

        @Test
        fun `should fail depositing a negative amount to an account`() {
            val account = buildAccount(100.toBigDecimal())

            val fail = account.deposit((-100).toBigDecimal())

            assertThat(fail).isEqualTo(Left(NegativeAmount))
        }
    }

    @Nested
    inner class Withdraw {

        @Test
        fun `should withdraw money from an account`() {
            val account = buildAccount(100.toBigDecimal())

            val updatedAccount = account.withdraw(50.toBigDecimal())

            assertThat(updatedAccount).isEqualTo(Right(buildAccount(50.toBigDecimal())))
        }

        @Test
        fun `should fail withdrawing a negative amount to an account`() {
            val account = buildAccount(100.toBigDecimal())

            val fail = account.withdraw((-50).toBigDecimal())

            assertThat(fail).isEqualTo(Left(NegativeAmount))
        }

        @Test
        fun `should fail withdrawing when there is not enough funds`() {
            val account = buildAccount(100.toBigDecimal())

            val fail = account.withdraw(200.toBigDecimal())

            assertThat(fail).isEqualTo(Left(NotEnoughFunds))
        }
    }


}