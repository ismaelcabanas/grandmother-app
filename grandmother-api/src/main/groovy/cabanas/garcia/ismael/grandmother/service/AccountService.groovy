package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction

/**
 * Created by XI317311 on 09/12/2016.
 */
interface AccountService {

    Account open(String accountNumber)

    Account open(String accountNumber, BigDecimal balance)

    @Deprecated
    Account deposit(Long accountId, String personId, BigDecimal amount, Date date)

    Account deposit(Long accountId, Deposit deposit)

    @Deprecated
    Account payment(Long accountId, String chargeTypeId, BigDecimal amount, Date date)

    Account payment(Long accountId, Payment payment)

    Account get(Long accountId)

    Collection<DepositTransaction> getDepositTransactions(Long accountId)

    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId)

    Collection<DepositTransaction> getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year)

    Collection<PaymentTransaction> getPaymentTransactionsByYearAndMonth(Long accountId, int year, int month)
}