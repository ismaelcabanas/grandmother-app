package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
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
            AccountRepository accountRepository = Mock(AccountRepository)
            BigDecimal balanceExpected = AmountUtil.TWENTY_THOUSAND
            accountRepository.balance(_,_,_) >> balanceExpected
            AccountBalanceService sut =
                    new RepositoryAccountBalanceService(accountRepository)
        and: "year and month for consulting payment transactions"
            int year = DateUtil.yearOf(DateUtil.TODAY)
            int month = DateUtil.monthOf(DateUtil.TODAY)
        when:
            BigDecimal actual = sut.balance(account.id, year, month)
        then:
            //1 * accountRepository.balance(account.id, year, month)
            actual == balanceExpected
    }
}
