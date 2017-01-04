package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import groovy.json.JsonSlurper
import org.springframework.mock.web.MockHttpServletResponse

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountTestUtils {

    static final BigDecimal DEFAULT_BALANCE = new BigDecimal(30000)
    static final String DEFAULT_ACCOUNT_NUMBER = "123"
    static final Long DEFAULT_ID = 1L

    static final BigDecimal ZERO = BigDecimal.ZERO
    static final BigDecimal TEN_THOUSAND = new BigDecimal(10000)
    static final BigDecimal TWENTY_THOUSAND = new BigDecimal(20000)
    static final BigDecimal THIRTY_THOUSAND = new BigDecimal(30000)

    static Account getDefaultAccount() {
        Account.builder()
            .id(DEFAULT_ID)
            .balance(DEFAULT_BALANCE)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .transactions(new Transactions())
            .build()
    }

    static deposit(Account account, Deposit deposit) {
        account.deposit(deposit)
    }

    static payment(Account account, Payment payment) {
        account.charge(payment)
    }


}
