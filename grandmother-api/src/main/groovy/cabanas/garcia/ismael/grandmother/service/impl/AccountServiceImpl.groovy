package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Charge
import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 09/12/2016.
 */
@Service
class AccountServiceImpl implements AccountService{

    @Autowired
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
        open(accountNumber, BigDecimal.ZERO)
    }

    @Override
    Account open(String accountNumber, BigDecimal balance) {
        Account account = Account.open(accountNumber, balance)

        accountRepository.save(account)

        return account
    }

    @Override
    Account get(String accountId) {
        return null
    }
}
