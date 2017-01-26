package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.assertions.DepositTransactionAssert
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by XI317311 on 25/01/2017.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
class DepositTransactionRepositoryITSpec extends Specification{

    @Autowired
    AccountRepository accountRepository

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    def "findByAccountIdOrderByDateOfMovementAsc() should return deposit transactions"(){
        given: "an account"
            Account account = new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "deposits on account made for persons"
            def depositFromIsmaelOf10000Today = DepositUtil.depositFromIsmaelOf10000Today()
            def depositFromBeaOf20000Yesterday = DepositUtil.depositFromBeaOf20000Yesterday()
            account.deposit(depositFromIsmaelOf10000Today)
            account.deposit(depositFromBeaOf20000Yesterday)
        and: "persist account in repository"
            accountRepository.save(account)
        when: "find deposits on account"
            Collection<DepositTransaction> deposits =
                depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(account.id)
        then:
            assert deposits != null
            assert deposits.size() == 2
            DepositTransactionAssert.assertThat(deposits).containsInOrder(depositFromBeaOf20000Yesterday, depositFromIsmaelOf10000Today)
    }

    def "findByAccountIdOrderByDateOfMovementAsc() should return empty deposit transactions when dont exist deposit transaction on account"(){
        given: "an account without deposits on it"
            Account account = new Account(accountNumber: AccountUtil.DEFAULT_ACCOUNT_NUMBER)
        and: "persist account in repository"
            accountRepository.save(account)
        when: "find deposits on account"
            Collection<DepositTransaction> deposits =
                depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(account.id)
        then:
            assert deposits != null
            assert deposits.isEmpty()
    }
}
