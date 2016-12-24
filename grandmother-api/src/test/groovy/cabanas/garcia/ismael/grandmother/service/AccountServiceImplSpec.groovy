package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.*
import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeUtilTest.*

/**
 * Created by XI317311 on 20/12/2016.
 */
class AccountServiceImplSpec extends Specification{

    def "should update account in repository when does a payment the account"(){
        given:
            String accountId = "1"
        and:
            AccountRepository mockAccountRepository = Mock(AccountRepository)
            mockAccountRepository.findOne(accountId) >> AccountTestUtils.getDefaultAccount()
            AccountService accountService = new AccountServiceImpl(accountRepository: mockAccountRepository)
        and:
            Payment paymentData = Payment.builder()
                    .amount(2000)
                    .date(TODAY)
                    .description("Payment")
                    .type(WATER_PAYMENT)
                    .build()
        when:
            accountService.payment(accountId, paymentData)
        then:
            1 * mockAccountRepository.save(_)
    }

    def "should find account in repository when gets account by identifier"(){
        given:
            String accountId = "1"
        and:
            AccountRepository mockAccountRepository = Mock(AccountRepository)
            AccountService accountService = new AccountServiceImpl(accountRepository: mockAccountRepository)
        when:
            accountService.get(accountId)
        then:
        1 * mockAccountRepository.findOne(accountId)
    }

    def "should return deposit transactions by date ordered ascending"(){
        given:
            String accountId = "1"
        and:
            AccountRepository accountRepository = Mock(AccountRepository)
            DepositTransactionRepository depositTransactionRepository = Mock(DepositTransactionRepository)
            AccountService accountService = new AccountServiceImpl(accountRepository: accountRepository,
                depositTransactionRepository: depositTransactionRepository)
        when:
            Collection<DepositTransaction> depositTransactions = accountService.getDepositTransactions(accountId)
        then:
            1 * depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(accountId)
    }
}
