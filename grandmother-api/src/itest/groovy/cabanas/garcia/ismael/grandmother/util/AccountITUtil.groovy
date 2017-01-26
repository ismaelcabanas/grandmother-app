package cabanas.garcia.ismael.grandmother.util

import cabanas.garcia.ismael.grandmother.Profiles
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Created by XI317311 on 25/01/2017.
 */
@Configuration
@Profile(Profiles.INTEGRATION)
class AccountITUtil {

    @Autowired
    AccountRepository accountRepository

    Account createDefault(){
        accountRepository.save(new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER))
    }

    def update(Account account) {
        accountRepository.save(account)
    }
}
