package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.TestConfiguration
import cabanas.garcia.ismael.grandmother.domain.account.*
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryAccountService
import cabanas.garcia.ismael.grandmother.util.TestEntityManagerUtil
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.test.AccountUtil.getDefaultAccountPersisted
import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.oneYearBeforeFrom
import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.getWaterPayment
import static cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.*

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration(classes = TestConfiguration.class) // not mentioned by docs, but had to include this for Spock to startup the Spring context
@DataJpaTest
//@DirtiesContext // What it does is mark the ApplicationContext as dirty, thus requiring it to be reloaded for the next integration test
class AccountServiceITSpec extends Specification{

    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy"

    @Autowired
    AccountRepository accountRepository

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    @Autowired
    TestEntityManager testEntityManager

    private TestEntityManagerUtil testEntityManagerUtil

    def setup(){
        testEntityManagerUtil = new TestEntityManagerUtil(testEntityManager: testEntityManager)
    }

    def "open an account in the system"(){
        given: "an account number"
            String accountNumber = AccountUtil.DEFAULT_ACCOUNT_NUMBER
        and: "the account service"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository)
        when: "create account"
            Account account = accountService.open(accountNumber)
        then:
            account.balance() == AmountUtil.ZERO
            account.accountNumber() == AccountUtil.DEFAULT_ACCOUNT_NUMBER
            account.getId() != null
    }

    def "a person does a deposit on given account"(){
        given: "an new account"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository)
            Account account = accountService.open(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "a given existing person in the system"
            Person ismael = testEntityManagerUtil.persist(getIsmael())
        and: "an amount"
            BigDecimal depositAmount = AmountUtil.THIRTY_THOUSAND
        and: "date of deposit"
            Date dateOfDeposit = DateUtil.TODAY
        when: "deposits on account"
            Deposit deposit = new Deposit(amount: depositAmount, date: dateOfDeposit, person: ismael, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), deposit)
        then:
            account.balance() == depositAmount

    }

    def "a charge on given account"(){
        given: "an new account"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository)
            Account account = accountService.open(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "a given existing amount in the system"
            PaymentType waterCharge = testEntityManagerUtil.persist(getWaterPayment())
        and: "an amount amount"
            BigDecimal paymentAmount = AmountUtil.THIRTY_THOUSAND
        and: "date of amount"
            Date dateOfCharge = DateUtil.TODAY
        when: "debits on account"
            Payment payment = new Payment(amount: paymentAmount, date: dateOfCharge, description: "El Agua", type: waterCharge)
            account = accountService.payment(account.getId(), payment)
        then:
            account.balance() == paymentAmount.negate()
    }

    def "deposits and charges generates movements on an account "(){
        given: "an open account"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository)
            Account account = accountService.open(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "two persons: ismael and bea"
            Person ismael = testEntityManagerUtil.persist(getIsmael())
            Person bea = testEntityManagerUtil.persist(getBea())
        and: "a deposit done on account by ismael"
            BigDecimal amountDepositedByIsmael = AmountUtil.TWENTY_THOUSAND
            Date dateOfDepositByIsmael = Date.parse(DATE_FORMAT_PATTERN, "01/07/2016")
            Deposit despositIsmael = new Deposit(amount: amountDepositedByIsmael, date: dateOfDepositByIsmael, person: ismael, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), despositIsmael)
        and: "a deposit done on account by Bea"
            BigDecimal amountDepositedByBea = AmountUtil.TEN_THOUSAND
            Date dateOfDepositByBea = Date.parse(DATE_FORMAT_PATTERN, "01/08/2016")
            Deposit depositBea = new Deposit(amount: amountDepositedByBea, date: dateOfDepositByBea, person: bea, description: "Transferencia a su favor")
            account = accountService.deposit(account.getId(), depositBea)
        and: "a water's amount on account"
            BigDecimal waterChargeAmount = AmountUtil.TWENTY_THOUSAND
            PaymentType waterCharge = testEntityManagerUtil.persist(getWaterPayment())
            Date dateOfCharge = Date.parse(DATE_FORMAT_PATTERN, "15/07/2016")
            Payment waterPayment = new Payment(amount: waterChargeAmount, date: dateOfCharge, type: waterCharge, description: "El agua")
            account = accountService.payment(account.getId(), waterPayment)
        expect:
            account.balance == AmountUtil.TEN_THOUSAND
        and:
            account.transactions.count() == 3
    }

    def "should return deposit transactions ordered in ascending by date"(){
        given: "account service"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository,
                    depositTransactionRepository: depositTransactionRepository)
        and: "an account persisted in the system"
            Account account = accountService.open(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "a given person persisted in the system"
            Person person = testEntityManagerUtil.persist(getDefaultPerson())
        and: "that person does two deposits on account with unordered dates"
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: DateUtil.YESTERDAY, description: "Transferencia a su favor", person: person)
            Deposit deposit20000 = new Deposit(amount: AmountUtil.TWENTY_THOUSAND, date: DateUtil.TODAY, description: "Transferencia a su favor", person: person)
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

    def "should return deposit transactions by person ordered in ascending by date"(){
        given: "account service"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository,
                depositTransactionRepository: depositTransactionRepository)
        and: "an account persisted in the system"
            Account account = accountService.open(getDefaultAccountPersisted().accountNumber)
        and: "a ismael and bea persons persisted in the system"
            Person ismael = testEntityManagerUtil.persist(getIsmael())
            Person bea = testEntityManagerUtil.persist(getBea())
        and: "that ismael does two deposits on account"
            Deposit deposit10000 = new Deposit(amount: 10000, date: DateUtil.YESTERDAY, description: "Transferencia a su favor", person: ismael)
            Deposit deposit20000 = new Deposit(amount: 20000, date: DateUtil.TODAY, description: "Transferencia a su favor", person: ismael)
            accountService.deposit(account.id, deposit10000)
            accountService.deposit(account.id, deposit20000)
        and: "that bea does one deposit on account"
            Deposit deposit5000 = new Deposit(amount: 5000, date: DateUtil.TODAY, description: "Transfere", person: bea)
            accountService.deposit(account.id, deposit5000)
        when:
            Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactionsByPersonId(account.id, ismael.id)
        then:
            depositTransactions.size() == 2
            with(depositTransactions.getAt(0)){
                [amount, dateOfMovement, description, person.name] == [deposit10000.amount, deposit10000.date, deposit10000.description, deposit10000.person.name]
            }
            with(depositTransactions.getAt(1)){
                [amount, dateOfMovement, description, person.name] == [deposit20000.amount, deposit20000.date, deposit20000.description, deposit20000.person.name]
            }
    }

    def "should return empty deposit transactions by person ordered in ascending by date"(){
        given: "account service"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository,
                depositTransactionRepository: depositTransactionRepository)
        and: "an account persisted in the system"
            Account account = accountService.open(getDefaultAccountPersisted().accountNumber)
        and: "a ismael and bea persons persisted in the system"
            Person ismael = testEntityManagerUtil.persist(getIsmael())
            Person bea = testEntityManagerUtil.persist(getBea())
        and: "that ismael does two deposits on account"
            Deposit deposit10000 = new Deposit(amount: 10000, date: DateUtil.YESTERDAY, description: "Transferencia a su favor", person: ismael)
            Deposit deposit20000 = new Deposit(amount: 20000, date: DateUtil.TODAY, description: "Transferencia a su favor", person: ismael)
            accountService.deposit(account.id, deposit10000)
            accountService.deposit(account.id, deposit20000)
        when:
            Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactionsByPersonId(account.id, bea.id)
        then:
            depositTransactions.size() == 0
    }

    def "should return deposit transactions by person and year on account ordered in ascending by date"(){
        given: "account service"
            AccountService accountService = new RepositoryAccountService(accountRepository: accountRepository,
                depositTransactionRepository: depositTransactionRepository)
        and: "an account persisted in the system"
            Account account = accountService.open(getDefaultAccountPersisted().accountNumber)
        and: "a ismael and bea persons persisted in the system"
            Person ismael = testEntityManagerUtil.persist(getIsmael())
            Person bea = testEntityManagerUtil.persist(getBea())
        and: "that ismael does two deposits on account"
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: DateUtil.YESTERDAY, description: "Transferencia a su favor", person: ismael)
            Deposit deposit20000 = new Deposit(amount: AmountUtil.TWENTY_THOUSAND, date: DateUtil.TODAY, description: "Transferencia a su favor", person: ismael)
            Deposit deposit30000 = new Deposit(amount: AmountUtil.THIRTY_THOUSAND, date: oneYearBeforeFrom(DateUtil.YESTERDAY), description: "Transferencia a su favor", person: bea)
            accountService.deposit(account.id, deposit10000)
            accountService.deposit(account.id, deposit20000)
            accountService.deposit(account.id, deposit30000)
        when:
            def year = DateUtil.yearOf(DateUtil.TODAY)
            Collection<DepositTransaction> depositTransactions =
                    accountService.getDepositTransactionsByPersonIdAndYear(account.id, ismael.id, year)
        then:
            depositTransactions.size() == 2
    }

}
