package monads.either

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import monads.either.AccountError.AccountNotFound
import monads.either.AccountError.NegativeAmount
import monads.either.AccountError.NotEnoughFunds
import monads.either.Either.Left
import monads.either.Either.Right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID


class ServicesTest {

    @Nested
    inner class TransferMoneyTest {

        private val transferMoney = TransferMoney()

        @Test
        fun `should transfer money within two different accounts`() {
            val debtor = buildAccount(100.toBigDecimal())
            val creditor = buildAccount(100.toBigDecimal())
            val amount = 50.toBigDecimal()

            val result = transferMoney(debtor, creditor, amount)

            assertThat(result).isEqualTo(Right(Pair(buildAccount(50.toBigDecimal()), buildAccount(150.toBigDecimal()))))
        }

        @Test
        fun `should fail transferring money within two different accounts when debtor has not enough funds`() {
            val debtor = buildAccount(100.toBigDecimal())
            val creditor = buildAccount(100.toBigDecimal())
            val amount = 500.toBigDecimal()

            val result = transferMoney(debtor, creditor, amount)

            assertThat(result).isEqualTo(Left(NotEnoughFunds))
        }
    }

    @Nested
    inner class DepositCashTest {

        private val accountRepository = mockk<AccountRepository>(relaxed = true)

        private val depositCash = DepositCash(accountRepository)

        @Test
        fun `should deposit cash`() {
            val account = buildAccount(100.toBigDecimal())
            val amount = 50.toBigDecimal()
            val userId = UUID.randomUUID()
            every { accountRepository.findBy(userId) } returns Right(account)

            val result = depositCash(userId, amount)

            assertThat(result).isEqualTo(Right(Unit)) // just side effects
            verify { accountRepository.save(account.copy(balance = 150.toBigDecimal())) }
        }

        @Test
        fun `should fail depositing cash when account is not found`() {
            val amount = 50.toBigDecimal()
            val userId = UUID.randomUUID()
            every { accountRepository.findBy(userId) } returns Left(AccountNotFound)

            val result = depositCash(userId, amount)

            assertThat(result).isEqualTo(Left(AccountNotFound))
        }

        @Test
        fun `should fail depositing cash when amount is invalid`() {
            val account = buildAccount(100.toBigDecimal())
            val negativeAmount = (-50).toBigDecimal()
            val userId = UUID.randomUUID()
            every { accountRepository.findBy(userId) } returns Right(account)

            val result = depositCash(userId, negativeAmount)

            assertThat(result).isEqualTo(Left(NegativeAmount))
        }
    }
}