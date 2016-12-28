package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils

/**
 * Created by XI317311 on 18/12/2016.
 */
class AccountServiceDefaultAccountStub extends AccountServiceStub{

    Account defaultAccount = AccountTestUtils.getDefaultAccount()

    @Override
    Account open(String accountNumber) {
        return defaultAccount
    }

    @Override
    Account open(String accountNumber, BigDecimal balance) {
        return defaultAccount
    }

    @Override
    Account deposit(Long accountId, String personId, BigDecimal amount, Date date) {
        return defaultAccount
    }

    @Override
    Account payment(Long accountId, String chargeTypeId, BigDecimal amount, Date date) {
        return defaultAccount
    }

    @Override
    Account get(Long accountId) {
        return defaultAccount
    }

    @Override
    Account deposit(Long accountId, Deposit deposit) {
        return defaultAccount
    }

    @Override
    Account payment(Long accountId, Payment payment) {
        return defaultAccount
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId) {
        return null
    }
}
