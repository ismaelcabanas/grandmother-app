package cabanas.garcia.ismael.grandmother.stubs.service.account

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService

class AccountBalanceServiceDefaultStub implements AccountBalanceService{
    Account account

    AccountBalanceServiceDefaultStub() {
    }

    AccountBalanceServiceDefaultStub(Account account) {
        this.account = account
    }

    @Override
    BigDecimal balance(long accountId, int year, int month) {
        if(account != null && account.id.equals(accountId))
            return account.balance
        return BigDecimal.ZERO
    }
}
