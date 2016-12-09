package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Charge
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import cabanas.garcia.ismael.grandmother.service.impl.PersonServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.Instant

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
class AccountServiceITSpec extends Specification{

    @Autowired
    AccountRepository accountRepository

    @Autowired
    PersonRepository personRepository

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

    def "a peson does a deposit on given account"(){
        given: "an new account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "a given existing person in the system"
            Person ismael = new Person(name: "Ismael")
            PersonService personService = new PersonServiceImpl(personRepository: personRepository)
            personService.create(ismael)
        and: "an amount and date of deposit"
            BigDecimal depositAmount = 30.000
            Date dateOfDeposit = now()
        when: "deposits on account"
            account = accountService.deposit(account.getId(), ismael.getId(), depositAmount, dateOfDeposit)
        then:
            account.balance() == depositAmount

    }
/*
    def "a debits on given account"(){
        given: "an new account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "a given existing charge in the system"
            Charge charge = new Charge()
        and: "an amount"
        BigDecimal depositAmount = 30.000
        when: "debits on account"
        account = accountService.debit(account.getId(), ismael.getId(), depositAmount)
        then:
        account.balance() == depositAmount

    }
*/
    private Date now() {
        Date.from(Instant.now())
    }
}
