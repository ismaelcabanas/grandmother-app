package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.service.AccountService

/**
 * Created by XI317311 on 15/12/2016.
 */
abstract class AccountServiceStub implements AccountService{
    @Override
    Account open(String accountNumber) {
        return null
    }

    @Override
    Account open(String accountNumber, BigDecimal balance) {
        return null
    }

    @Override
    Account deposit(Long accountId, String personId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account payment(Long accountId, String chargeTypeId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account get(Long accountId) {
        return null
    }

    @Override
    Account deposit(Long accountId, Deposit deposit) {
        return null
    }

    @Override
    Account payment(Long accountId, Payment payment) {
        return null
    }

    @Override
    Collection<DepositTransaction> getDepositTransactions(Long accountId) {
        return null
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId) {
        return null
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year) {
        return null
    }

    @Override
    Collection<PaymentTransaction> getPaymentTransactionsByYearAndMonth(Long accountId, int year, int month) {
        return null
    }
}
