package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.TestConfiguration
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
import cabanas.garcia.ismael.grandmother.utils.test.PaymentUtil
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = TestConfiguration.class) // not mentioned by docs, but had to include this for Spock to startup the Spring context
@DataJpaTest
class TransactionRepositoryITSpec extends Specification{

    @Autowired
    TestEntityManager testEntityManager

    @Autowired
    TransactionRepository sut

    @Unroll
    def "findByAccountIdAndDateOfMovementLessThan() should return transactions for an account until the last day of month #month of the year #year"(){
        given: "an account with transactions persisted in the repository"
            Account account = new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER)
            Deposit depositFromIsmaelOf10000At20100115 = DepositUtil.depositFrom("Ismael", AmountUtil.TEN_THOUSAND, "2010-01-15 00:00:00.0")
            Deposit depositFromIsmaelOf10000At20090406 = DepositUtil.depositFrom("Ismael", AmountUtil.TEN_THOUSAND, "2009-04-06 00:00:00.0")
            Payment paymentOfWaterOf10000At20100101 = PaymentUtil.paymentOf("Water", AmountUtil.TEN_THOUSAND, "2010-01-01 00:00:00.0")
            account.deposit(depositFromIsmaelOf10000At20090406)
            account.deposit(depositFromIsmaelOf10000At20100115)
            account.charge(paymentOfWaterOf10000At20100101)
            testEntityManager.persist(account)
        when: "find transactions for account less than a date"
            Date endDate = calculateEndDateOf(year, month)
            Collection<Transaction> transactions = sut.findByAccountIdAndDateOfMovementLessThan(account.id, endDate)
        then:
            assert transactions.size() == numberOfTransactionsExpected
        where:
        year | month | numberOfTransactionsExpected
        2010 | 1     | 3
        1999 | 3     | 0
    }

    Date calculateEndDateOf(int year, int month) {
        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0)
        DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
        lastDayOfMonth.toDate()
    }
}
