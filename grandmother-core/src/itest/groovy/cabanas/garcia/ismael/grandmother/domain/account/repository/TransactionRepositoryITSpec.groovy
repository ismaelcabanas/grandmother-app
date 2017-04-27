package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.TestConfiguration
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
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
    def "balance() should return balance for an account with transactions until the year #year and month #month"(){
        given: "an account"
            Account account = new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "deposits on account made for persons"
            def depositFromIsmaelOf10000Today = DepositUtil.depositFromIsmaelOf10000Today()
            def depositFromBeaOf20000Yesterday = DepositUtil.depositFromBeaOf20000Yesterday()
            account.deposit(depositFromIsmaelOf10000Today)
            account.deposit(depositFromBeaOf20000Yesterday)
        and: "persist account in repository"
            testEntityManager.persist(account)
        and: "dates are"
            DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0)
            DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
            Date endDate = lastDayOfMonth.toDate()
        when: "find transactions on account"
            BigDecimal balance =
                sut.balance(account.id, endDate)
        then:
            assert balance == balanceExpected
        where:
        year                            | month                            | balanceExpected
        DateUtil.yearOf(DateUtil.TODAY) | DateUtil.monthOf(DateUtil.TODAY) | AmountUtil.THIRTY_THOUSAND
        1999                            | 3                                | null
    }
}
