package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.service.AccountService

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountServiceThatGetAnAccountStub implements AccountService {

    Account account

    @Override
    Account open(String accountNumber, BigDecimal balance) {
        return null
    }

    @Override
    Account deposit(String accountId, String personId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account charge(String accountId, String chargeTypeId, BigDecimal amount, Date date) {
        return null
    }

    @Override
    Account get(String accountId) {
        return null
    }
}
