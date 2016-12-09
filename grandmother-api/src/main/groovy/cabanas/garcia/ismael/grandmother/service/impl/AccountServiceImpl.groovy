package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.service.AccountService

/**
 * Created by XI317311 on 09/12/2016.
 */
class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository

    @Override
    Account open(String accountNumber) {
        Account account = Account.open(accountNumber)

        accountRepository.save(account)

        return account
    }
}
