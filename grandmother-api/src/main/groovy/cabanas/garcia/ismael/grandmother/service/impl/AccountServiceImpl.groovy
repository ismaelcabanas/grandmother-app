package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.AccountService

/**
 * Created by XI317311 on 09/12/2016.
 */
class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository

    @Override
    Account deposit(String accountId, String personId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)

        Person person = Person.builder().id(personId).build()
        Deposit deposit = Deposit.builder().amount(amount).person(person).date(date).build()
        account.deposit(deposit)

        accountRepository.save(account)
    }

    @Override
    Account open(String accountNumber) {
        Account account = Account.open(accountNumber)

        accountRepository.save(account)

        return account
    }
}
