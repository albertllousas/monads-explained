package monads.either

import java.math.BigDecimal

// needed because constructor is private
fun buildAccount(initialAmount: BigDecimal): Account =
    Account::class.java.getDeclaredConstructor(BigDecimal::class.java)
        .also { it.isAccessible = true }
        .newInstance(initialAmount)