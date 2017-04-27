package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.TransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryAccountBalanceService
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DateUtil
import spock.lang.Specification

class RepositoryAccountBalanceServiceSpec extends Specification{

    def "should return balance of an account to given year and month"(){
        given: "a given account with transactions"
            Account account = AccountUtil.getDefaultAccountPersisted()
        and:
            TransactionRepository transactionRepository = Mock(TransactionRepository)
            BigDecimal balanceExpected = AmountUtil.TWENTY_THOUSAND
            transactionRepository.balance(_,_) >> balanceExpected
            AccountBalanceService sut =
                    new RepositoryAccountBalanceService(transactionRepository)
        and: "year and month for consulting payment transactions"
            int year = DateUtil.yearOf(DateUtil.TODAY)
            int month = DateUtil.monthOf(DateUtil.TODAY)
        when:
            BigDecimal actual = sut.balance(account.id, year, month)
        then:
            //1 * transactionRepository.balance(account.id, year, month)
            actual == balanceExpected
    }

    def "should return zero balance of an account unitl given year and month if not exist transactions to that date"(){
        given: "a given account with transactions"
            Account account = AccountUtil.getDefaultAccountPersisted()
        and:
            TransactionRepository transactionRepository = Mock(TransactionRepository)
            BigDecimal balanceExpected = AmountUtil.ZERO
            transactionRepository.balance(_,_) >> null
            AccountBalanceService sut =
                    new RepositoryAccountBalanceService(transactionRepository)
        and: "year and month for consulting payment transactions"
            int year = DateUtil.yearOf(DateUtil.TODAY)
            int month = DateUtil.monthOf(DateUtil.TODAY)
        when:
            BigDecimal actual = sut.balance(account.id, year, month)
        then:
            actual == balanceExpected
    }
}
