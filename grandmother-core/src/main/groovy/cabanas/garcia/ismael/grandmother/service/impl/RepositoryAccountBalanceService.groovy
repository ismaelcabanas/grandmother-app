package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService

class RepositoryAccountBalanceService implements AccountBalanceService{
    AccountRepository accountRepository

    RepositoryAccountBalanceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository
    }

    @Override
    BigDecimal balance(long accountId, int year, int month) {
        return accountRepository.balance(accountId, year, month)
    }
}
