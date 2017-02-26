package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.Transactions

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountUtil {

    static final BigDecimal DEFAULT_BALANCE = AmountUtil.THIRTY_THOUSAND
    static final String DEFAULT_ACCOUNT_NUMBER = "123"
    static final Long DEFAULT_ID = 1L


    static Account getDefaultAccountPersisted() {
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
