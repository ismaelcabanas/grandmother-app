package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository extends CrudRepository<Account, Long>{

    BigDecimal balance(long accountId, int year, int month)
}