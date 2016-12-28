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
    Account payment(Long accountId, String chargeTypeId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)
        
        PaymentType chargeType = PaymentType.builder().id(chargeTypeId).build()
        Payment charge = Payment.builder().type(chargeType).amount(amount).date(date).build()
        account.charge(charge)

        accountRepository.save(account)

        return account
    }

    @Override
    Account payment(Long accountId, Payment payment) {
        Account account = accountRepository.findOne(accountId)

        account.charge(payment)

        accountRepository.save(account)

        return account
    }

    @Override
    Account deposit(Long accountId, String personId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)

        Person person = Person.builder().id(personId).build()
        Deposit deposit = Deposit.builder().amount(amount).person(person).date(date).build()
        account.deposit(deposit)

        accountRepository.save(account)

        return account
    }

    @Override
    Account deposit(Long accountId, Deposit deposit) {
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

        log.debug("Account persited $account")

        return account
    }

    @Override
    Account get(Long accountId) {
        log.debug("Getting account $accountId")

        Account account = accountRepository.findOne(accountId)

        log.debug("Account got $account")

        return account
    }

    @Override
    Collection<DepositTransaction> getDepositTransactions(Long accountId) {
        return depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(accountId)
    }

    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId) {
        return depositTransactionRepository.findByAccountIdAndPersonIdOrderByDateOfMovementAsc(accountId, personId)
    }
}
