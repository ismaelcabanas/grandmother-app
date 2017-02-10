package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryAccountService
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*

/**
 * Created by XI317311 on 20/12/2016.
 */
class RepositoryAccountServiceSpec extends Specification{

    def "should update account in repository when does a payment the account"(){
        given:
            Long accountId = 1
        and:
            AccountRepository mockAccountRepository = Mock(AccountRepository)
            mockAccountRepository.findOne(accountId) >> AccountUtil.getDefaultAccountPersisted()
            AccountService accountService = new RepositoryAccountService(accountRepository: mockAccountRepository)
        and:
            Payment paymentData = Payment.builder()
                    .amount(2000)
                    .date(cabanas.garcia.ismael.grandmother.utils.test.DateUtil.TODAY)
                    .description("Payment")
                    .type(PaymentTypeUtil.WATER_PAYMENT)
                    .build()
        when:
            accountService.payment(accountId, paymentData)
        then:
            1 * mockAccountRepository.save(_)
    }

    def "should find account in repository when gets account by identifier"(){
        given:
            Long accountId = 1
        and:
            AccountRepository mockAccountRepository = Mock(AccountRepository)
            AccountService accountService = new RepositoryAccountService(accountRepository: mockAccountRepository)
        when:
            accountService.get(accountId)
        then:
            1 * mockAccountRepository.findOne(accountId)
    }

    def "should return deposit transactions by person ordered ascending by date"(){
        given:
            Long accountId = 1
            Long personId = 1
        and:
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            AccountService accountService = new RepositoryAccountService(
                    depositTransactionRepository: depositTransactionRepository)
        when:
            accountService.getDepositTransactionsByPersonId(accountId, personId)
        then:
            1 * depositTransactionRepository.findByAccountIdAndPersonIdOrderByDateOfMovementAsc(accountId, personId)

    }

    def "should return deposit transactions by person and year ordered ascending by date"(){
        given:
            Long accountId = 1
            Long personId = 1
            int year = 2016
        and:
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            AccountService accountService = new RepositoryAccountService(
                    depositTransactionRepository: depositTransactionRepository)
        when:
            accountService.getDepositTransactionsByPersonIdAndYear(accountId, personId, year)
        then:
            1 * depositTransactionRepository.findByAccountIdAndPersonIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(_, _, _, _)
    }

}
