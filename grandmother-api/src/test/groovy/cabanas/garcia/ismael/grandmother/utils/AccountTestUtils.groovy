package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Transactions

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountTestUtils {

    static BigDecimal DEFAULT_BALANCE = new BigDecimal(30000)
    static String DEFAULT_ACCOUNT_NUMBER = "123"

    static Account getDefaultAccount() {
        Account.builder()
            .balance(DEFAULT_BALANCE)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .transactions(new Transactions())
            .build()
    }

}
