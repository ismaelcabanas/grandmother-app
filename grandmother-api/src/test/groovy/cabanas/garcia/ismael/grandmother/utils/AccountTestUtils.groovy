package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Transactions

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountTestUtils {

    static BigDecimal DEFAULT_BALANCE = new BigDecimal(30000)
    static String DEFAULT_ACCOUNT_NUMBER = "123"
    static Long DEFAULT_ID = 1L

    static Account getDefaultAccount() {
        Account.builder()
            .id(DEFAULT_ID)
            .balance(DEFAULT_BALANCE)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .transactions(new Transactions())
            .build()
    }

}
