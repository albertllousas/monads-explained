package monads.either

import java.math.BigDecimal
import java.util.UUID

class TransferMoney {
    operator fun invoke(debtor: Account, creditor: Account, amount: BigDecimal): Either<AccountError, Pair<Account, Account>> =
        debtor
            .withdraw(amount)
            .flatMap { d -> creditor.deposit(amount).map { Pair(d, it) } }
}

class DepositCash(private val repository: AccountRepository) {
    operator fun invoke(userId: UUID, amount: BigDecimal): Either<AccountError, Unit> =
        repository.findBy(userId)
            .flatMap { it.deposit(amount) }
            .map(repository::save)
}