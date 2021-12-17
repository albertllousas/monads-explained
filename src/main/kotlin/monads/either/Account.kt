package monads.either

import monads.either.AccountError.AccountNotFound
import monads.either.AccountError.NegativeAmount
import monads.either.AccountError.NotEnoughFunds
import monads.either.Either.Left
import monads.either.Either.Right
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID

data class Account private constructor(val balance: BigDecimal) {
    companion object {
        fun create(initialBalance: BigDecimal): Either<NegativeAmount, Account> =
            applyAmount(initialBalance) { Account(it) }

        private fun applyAmount(amount: BigDecimal, fn: (BigDecimal) -> Account) =
            if (amount < ZERO) Left(NegativeAmount) else Right(fn(amount))
    }

    fun deposit(amount: BigDecimal): Either<NegativeAmount, Account> =
        applyAmount(amount) { this.copy(balance = this.balance + it) }

    fun withdraw(amount: BigDecimal): Either<AccountError, Account> =
        applyAmount(amount) { this.copy(balance = this.balance - it) }
            .flatMap {
                if ((balance - amount) < ZERO) Left(NotEnoughFunds) else Right(Account(balance - amount))
            }
}

sealed class AccountError {

    object NegativeAmount : AccountError()
    object NotEnoughFunds : AccountError()
    object AccountNotFound : AccountError()
}

interface AccountRepository {
    fun findBy(userId: UUID): Either<AccountNotFound, Account>
    fun save(account: Account)
}
