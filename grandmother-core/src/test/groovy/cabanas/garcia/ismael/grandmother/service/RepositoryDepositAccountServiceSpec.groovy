package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryDepositAccountService
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.*

/**
 * Created by XI317311 on 05/01/2017.
 */
class RepositoryDepositAccountServiceSpec extends Specification{

    def "should return deposits transactions for given account in ascending order by date"(){
        given: "given account"
            Account account = Account.builder().id(1).build()
        and: "deposit transactions for given account in repository"
            Collection<DepositTransaction> depositTransactionsInRepository = new ArrayList<>()
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TEN_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.TODAY, person: cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getPersistedIsmael()))
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TEN_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.YESTERDAY, person: cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getPersistedBea()))
        and: "the deposit account service"
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            DepositAccountService depositAccountService =
                    new RepositoryDepositAccountService(depositTransactionRepository: depositTransactionRepository)
        when: "get deposit transactions for given account"
            Transactions depositTransactions = depositAccountService.getDepositTransactions(account.id)
        then:
            1 * depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(account.id) >> depositTransactionsInRepository
        and:
            depositTransactions.total == AmountUtil.TWENTY_THOUSAND
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

    def "should return deposits transactions for given account and person in ascending order by date"(){
        given: "given account"
            Account account = Account.builder().id(1).build()
        and: "a person called ismael"
            Person ismael = cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getPersistedIsmael()
        and: "deposit transactions made for ismael in given account"
            Collection<DepositTransaction> depositTransactionsInRepository = new ArrayList<>()
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TEN_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.TODAY, person: ismael))
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TEN_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.YESTERDAY, person: ismael))
        and: "the deposit account service"
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            DepositAccountService depositAccountService =
                    new RepositoryDepositAccountService(depositTransactionRepository: depositTransactionRepository)
        when: "get deposit transactions made from ismael for given account"
            Transactions depositTransactions = depositAccountService.getDepositTransactionsByPersonId(account.id, ismael.id)
        then:
            1 * depositTransactionRepository.findByAccountIdAndPersonIdOrderByDateOfMovementAsc(account.id, ismael.id) >> depositTransactionsInRepository
        and:
            depositTransactions.total == AmountUtil.TWENTY_THOUSAND
            depositTransactions.areEmpty() == false
            depositTransactions.list != null
            depositTransactions.count() == 2
    }

    def "should return deposits transactions for given account, person and year in ascending order by date"(){
        given: "given account"
            Account account = Account.builder().id(1).build()
        and: "a person called ismael"
            Person ismael = cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getPersistedIsmael()
        and: "deposit transactions made for ismael in given account at this year"
            Collection<DepositTransaction> depositTransactionsInRepository = new ArrayList<>()
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TEN_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.TODAY, person: ismael))
            depositTransactionsInRepository.add(new DepositTransaction(account: account, amount: AmountUtil.TWENTY_THOUSAND, dateOfMovement: cabanas.garcia.ismael.grandmother.utils.test.DateUtil.YESTERDAY, person: ismael))
        and: "the deposit account service"
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            DepositAccountService depositAccountService =
                new RepositoryDepositAccountService(depositTransactionRepository: depositTransactionRepository)
            int year = cabanas.garcia.ismael.grandmother.utils.test.DateUtil.yearOf(cabanas.garcia.ismael.grandmother.utils.test.DateUtil.TODAY)
        when: "get deposit transactions made from ismael for given account and this year"
            Transactions depositTransactions = depositAccountService.getDepositTransactionsByPersonIdAndYear(account.id, ismael.id, year)
        then:
            1 * depositTransactionRepository.findByAccountIdAndPersonIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(account.id, ismael.id, _, _) >> depositTransactionsInRepository
        and:
        depositTransactions.total == AmountUtil.THIRTY_THOUSAND
        depositTransactions.areEmpty() == false
        depositTransactions.list != null
        depositTransactions.count() == 2
    }
}
