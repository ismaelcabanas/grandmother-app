package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Charge
import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
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
    Account charge(String accountId, String chargeTypeId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)
        
        ChargeType chargeType = ChargeType.builder().id(chargeTypeId).build()
        Charge charge = Charge.builder().type(chargeType).amount(amount).date(date).build()
        account.charge(charge)

        accountRepository.save(account)

        return account
    }

    @Override
    Account deposit(String accountId, String personId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)

        Person person = Person.builder().id(personId).build()
        Deposit deposit = Deposit.builder().amount(amount).person(person).date(date).build()
        account.deposit(deposit)

        accountRepository.save(account)

        return account
    }

    @Override
    Account open(String accountNumber) {
        Account account = Account.open(accountNumber)

        accountRepository.save(account)

        return account
    }
}
