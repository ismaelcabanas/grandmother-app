package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
class AccountServiceITSpec extends Specification{

    @Autowired
    AccountRepository accountRepository

    private static final String ACCOUNT_NUMBER = "ES6401820474280201551793"

    def "open an account in the system"(){
        given: "an account number"
            String accountNumber = ACCOUNT_NUMBER
        and: "the account service"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
        when: "create account"
            Account account = accountService.open(accountNumber)
        then:
            account.balance() == 0.000
            account.accountNumber() == ACCOUNT_NUMBER
            account.getId() != null
    }
    /*
    def "a peson deposit on given account"(){
        given: "a person does a deposit on an account"
            Person ismael = new Person(name: "Ismael")
            BigDecimal depositAmount = 30.000
            String accountIdentifier =
        and: "the account service"
            AccountService accountService = new AccountServiceImpl()
        when: "deposit"
            accountService.deposit()
        then:
    }
    */
}
