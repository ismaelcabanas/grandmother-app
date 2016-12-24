package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Ignore
import spock.lang.Specification

import java.time.Instant

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
@Transactional
@DirtiesContext // What it does is mark the ApplicationContext as dirty, thus requiring it to be reloaded for the next integration test
class AccountServiceITSpec extends Specification{

    public static final String WATER_CHARGE_TYPE = "Agua"
    public static final BigDecimal AMOUNT = 30.000
    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy"

    @Autowired
    AccountRepository accountRepository

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    @Autowired
    PersonService personService

    @Autowired
    PaymentTypeService chargeTypeService

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

    def "a person does a deposit on given account"(){
        given: "an new account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "a given existing person in the system"
            Person ismael = createPerson("Ismael")
        and: "an amount"
            BigDecimal depositAmount = AMOUNT
        and: "date of deposit"
            Date dateOfDeposit = now()
        when: "deposits on account"
            Deposit deposit = new Deposit(amount: depositAmount, date: dateOfDeposit, person: ismael, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), deposit)
        then:
            account.balance() == depositAmount

    }

    def "a charge on given account"(){
        given: "an new account"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository)
            Account account = accountService.open(ACCOUNT_NUMBER)
        and: "a given existing amount in the system"
            PaymentType waterCharge = createChargeType(WATER_CHARGE_TYPE)
        and: "an amount amount"
            BigDecimal paymentAmount = AMOUNT
        and: "date of amount"
            Date dateOfCharge = now()
        when: "debits on account"
            Payment payment = new Payment(amount: paymentAmount, date: dateOfCharge, description: "El Agua", type: waterCharge)
            account = accountService.payment(account.getId(), payment)
        then:
            account.balance() == paymentAmount.negate()
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
            Deposit despositIsmael = new Deposit(amount: amountDepositedByIsmael, date: dateOfDepositByIsmael, person: ismael, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), despositIsmael)
        and: "a deposit done on account by Bea"
            BigDecimal amountDepositedByBea = 10.000
            Date dateOfDepositByBea = Date.parse(DATE_FORMAT_PATTERN, "01/08/2016")
            Deposit depositBea = new Deposit(amount: amountDepositedByBea, date: dateOfDepositByBea, person: bea, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), depositBea)
        and: "a water's amount on account"
            BigDecimal waterChargeAmount = 20.000
            PaymentType waterCharge = createChargeType(WATER_CHARGE_TYPE)
            Date dateOfCharge = Date.parse(DATE_FORMAT_PATTERN, "15/07/2016")
            Payment waterPayment = new Payment(amount: waterChargeAmount, date: dateOfCharge, type: waterCharge, description: "El agua")
            account = accountService.payment(account.getId(), waterPayment)
        expect:
            account.balance == 10.000
        and:
            account.transactions.count() == 3
    }

    @Ignore
    def "should return deposit transactions ordered in ascending by date"(){
        given: "account service"
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository,
                    depositTransactionRepository: depositTransactionRepository)
        and: "an account persisted in the system"
            Account account = accountService.open(AccountTestUtils.getDefaultAccount().accountNumber)
        and: "a given person persisted in the system"
            Person person = personService.create(PersonUtilTest.getDefaultPerson())
        and: "that person does two deposits on account with unordered dates"
            Deposit deposit10000 = new Deposit(amount: 10000, date: DateUtilTest.YESTERDAY, description: "Transferencia a su favor", person: person)
            Deposit deposit20000 = new Deposit(amount: 20000, date: DateUtilTest.TODAY, description: "Transferencia a su favor", person: person)
            accountService.deposit(account.id, deposit10000)
            accountService.deposit(account.id, deposit20000)
        when:
            Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactions(account.id)
        then:
            depositTransactions.size() == 2
            with(depositTransactions.getAt(0)){
                [amount, dateOfMovement, description, person.name] == [deposit10000.amount, deposit10000.date, deposit10000.description, deposit10000.person.name]
            }
            with(depositTransactions.getAt(1)){
                [amount, dateOfMovement, description, person.name] == [deposit20000.amount, deposit20000.date, deposit20000.description, deposit20000.person.name]
            }
    }

    private def createChargeType(String name) {
        PaymentType waterCharge = new PaymentType(name: name)
        chargeTypeService.create(waterCharge)
    }

    private def createPerson(String name){
        Person aPerson = new Person(name: name)
        personService.create(aPerson)
    }
    private Date now() {
        Date.from(Instant.now())
    }
}
