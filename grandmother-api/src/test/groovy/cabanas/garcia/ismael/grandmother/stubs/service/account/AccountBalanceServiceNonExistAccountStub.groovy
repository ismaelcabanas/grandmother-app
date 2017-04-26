package cabanas.garcia.ismael.grandmother.stubs.service.account

import cabanas.garcia.ismael.grandmother.service.AccountBalanceService

class AccountBalanceServiceNonExistAccountStub implements AccountBalanceService{
    @Override
    BigDecimal balance(long accountId, int year, int month) {
        return null
    }
}
