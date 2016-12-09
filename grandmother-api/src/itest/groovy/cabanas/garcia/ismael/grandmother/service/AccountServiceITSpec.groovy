package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import cabanas.garcia.ismael.grandmother.service.impl.ChargeTypeServiceImpl
import cabanas.garcia.ismael.grandmother.service.impl.PersonServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.Instant

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
@Transactional
class AccountServiceITSpec extends Specification{

    public static final String WATER_CHARGE_TYPE = "Agua"
    public static final BigDecimal AMOUNT = 30.000
    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy"
    @Autowired
    AccountRepository accountRepository

    @Autowired
    PersonRepository personRepository

    @Autowired
    ChargeTypeRepository chargeTypeRepository

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
        and: "an amount"
            BigDecimal depositAmount = AMOUNT
        and: "date of deposit"
            Date dateOfDeposit = now()
        when: "deposits on account"
            account = accountService.deposit(account.getId(), ismael.getId(), depositAmount, dateOfDeposit)
        then:
            account.balance() == depositAmount

    }

    def "a charge on given account"(){
        given: "an new account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "a given existing charge in the system"
            ChargeType waterCharge = new ChargeType(name: WATER_CHARGE_TYPE)
            ChargeTypeService chargeTypeService = new ChargeTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
            chargeTypeService.create(waterCharge)
        and: "an charge amount"
            BigDecimal chargeAmount = AMOUNT
        and: "date of charge"
            Date dateOfCharge = now()
        when: "debits on account"
            account = accountService.charge(account.getId(), waterCharge.id, chargeAmount, dateOfCharge)
        then:
            account.balance() == chargeAmount.negate()
    }

    def "deposits and charges generates movements on an account "(){
        given: "an open account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "two persons: ismael and bea"
            Person ismael = createPerson("Ismael")
            Person bea = createPerson("Bea")
        and: "a deposit done on account by ismael"
            BigDecimal amountDepositedByIsmael = 20.000
            Date dateOfDepositByIsmael = Date.parse(DATE_FORMAT_PATTERN, "01/07/2016")
            account = accountService.deposit(account.getId(), ismael.getId(), amountDepositedByIsmael, dateOfDepositByIsmael)
        and: "a deposit done on account by Bea"
            BigDecimal amountDepositedByBea = 10.000
            Date dateOfDepositByBea = Date.parse(DATE_FORMAT_PATTERN, "01/08/2016")
            account = accountService.deposit(account.getId(), ismael.getId(), amountDepositedByBea, dateOfDepositByBea)
        and: "a water's charge on account"
            BigDecimal waterChargeAmount = 20.000
            ChargeType waterCharge = new ChargeType(name: WATER_CHARGE_TYPE)
            ChargeTypeService chargeTypeService = new ChargeTypeServiceImpl(chargeTypeRepository: chargeTypeRepository)
            chargeTypeService.create(waterCharge)
            Date dateOfCharge = Date.parse(DATE_FORMAT_PATTERN, "15/07/2016")
            account = accountService.charge(account.getId(), waterCharge.getId(), waterChargeAmount, dateOfCharge)
        expect:
            account.balance == 10.000
        and:
            account.movements.size() == 3

    }

    private def createPerson(String name){
        Person aPerson = new Person(name: name)
        PersonService personService = new PersonServiceImpl(personRepository: personRepository)
        personService.create(aPerson)
    }
    private Date now() {
        Date.from(Instant.now())
    }
}
