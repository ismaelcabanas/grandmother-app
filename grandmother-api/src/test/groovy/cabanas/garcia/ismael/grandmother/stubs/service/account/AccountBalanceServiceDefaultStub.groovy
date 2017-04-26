package cabanas.garcia.ismael.grandmother.stubs.service.account

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService

class AccountBalanceServiceDefaultStub implements AccountBalanceService{
    AccountBalanceServiceDefaultStub(Account account) {
    }

    @Override
    BigDecimal balance(long accountId, int year, int month) {
        return null
    }
}
