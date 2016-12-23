package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.*
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 09/12/2016.
 */
@Service
@Slf4j
class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository

    @Autowired
    private DepositTransactionRepository depositTransactionRepository

    @Override
    Account payment(String accountId, String chargeTypeId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)
        
        PaymentType chargeType = PaymentType.builder().id(chargeTypeId).build()
        Payment charge = Payment.builder().type(chargeType).amount(amount).date(date).build()
        account.charge(charge)

        accountRepository.save(account)

        return account
    }

    @Override
    Account payment(String accountId, Payment payment) {
        Account account = accountRepository.findOne(accountId)

        account.charge(payment)

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
    Account deposit(String accountId, Deposit deposit) {
        Account account = accountRepository.findOne(accountId)

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
        return accountRepository.findOne(accountId)
    }

    @Override
    Collection<DepositTransaction> getDepositTransactions(String accountId) {
        return depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(accountId)
    }
}
