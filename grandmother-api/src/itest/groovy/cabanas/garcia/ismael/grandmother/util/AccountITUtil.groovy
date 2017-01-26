package cabanas.garcia.ismael.grandmother.util

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil

/**
 * Created by XI317311 on 25/01/2017.
 */
final class AccountITUtil {

    static Account createDefault(AccountRepository accountRepository){
        accountRepository.save(new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER))
    }
}
