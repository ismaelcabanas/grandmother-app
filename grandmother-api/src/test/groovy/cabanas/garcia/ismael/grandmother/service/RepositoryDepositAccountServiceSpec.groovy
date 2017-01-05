package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryAccountService
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryDepositAccountService
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.*
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.*
import static cabanas.garcia.ismael.grandmother.utils.PersonUtilTest.*

/**
 * Created by XI317311 on 05/01/2017.
 */
class RepositoryDepositAccountServiceSpec extends Specification{

    def "should return deposits transactions for given account in ascending order by date"(){
        given: "given account"
            Account account = Account.builder().id(1).build()
        and: "deposit transactions for given account in repository"
            Collection<DepositTransaction> depositTransactionsInRepository = new ArrayList<>()
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: TEN_THOUSAND, dateOfMovement: TODAY, person: getIsmael()))
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: TEN_THOUSAND, dateOfMovement: YESTERDAY, person: getBea()))
        and: "the deposit account service"
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            DepositAccountService depositAccountService =
                    new RepositoryDepositAccountService(depositTransactionRepository: depositTransactionRepository)
        when: "get deposit transactions for given account"
            Transactions depositTransactions = depositAccountService.getDepositTransactions(account.id)
        then:
            1 * depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(account.id) >> depositTransactionsInRepository
        and:
            depositTransactions.total == TWENTY_THOUSAND
            depositTransactions.list != null
            depositTransactions.count() == 2
            depositTransactions.areEmpty() == false
    }

    def "should return empty deposits transactions for given account when the account don't have deposit transactions"(){
        given: "given account"
            Account account = Account.builder().id(1).build()
        and: "deposit transactions for given account in repository is empty"
            Collection<DepositTransaction> depositTransactionsInRepository = new ArrayList<>()
        and: "the deposit account service"
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            DepositAccountService depositAccountService =
                new RepositoryDepositAccountService(depositTransactionRepository: depositTransactionRepository)
        when: "get deposit transactions for given account"
            Transactions depositTransactions = depositAccountService.getDepositTransactions(account.id)
        then:
            1 * depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(account.id) >> depositTransactionsInRepository
        and:
            depositTransactions.areEmpty() == true
    }

}
