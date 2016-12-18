package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment
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
    Account deposit(String accountId, String personId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account payment(String accountId, String chargeTypeId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account get(String accountId) {
        return null
    }

    @Override
    Account deposit(String accountId, Deposit deposit) {
        return null
    }

    @Override
    Account payment(String accountId, Payment payment) {
        return null
    }
}
