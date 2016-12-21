package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.service.impl.AccountServiceImpl
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.*
import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeUtilTest.*
import org.mockito.Mock

/**
 * Created by XI317311 on 20/12/2016.
 */
class AccountServiceImplSpec extends Specification{

    def "should update account in repository when does a payment the account"(){
        given:
            String accountId = "1"
        and:
            AccountRepository mockAccountRepository = Mock(AccountRepository)
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
}
