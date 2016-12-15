package cabanas.garcia.ismael.grandmother.stubs.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountServiceThatGetAnAccountStub extends AccountServiceStub implements AccountService {

    Account account

    @Override
    Account get(String accountId) {
        return account
    }
}
