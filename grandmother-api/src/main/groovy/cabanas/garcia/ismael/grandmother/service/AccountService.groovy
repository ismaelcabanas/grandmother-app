package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment

/**
 * Created by XI317311 on 09/12/2016.
 */
interface AccountService {

    Account open(String accountNumber)

    Account open(String accountNumber, BigDecimal balance)

    @Deprecated
    Account deposit(String accountId, String personId, BigDecimal amount, Date date)

    Account deposit(String accountId, Deposit deposit)

    @Deprecated
    Account payment(String accountId, String chargeTypeId, BigDecimal amount, Date date)

    Account payment(String accountId, Payment payment)

    Account get(String accountId)
}