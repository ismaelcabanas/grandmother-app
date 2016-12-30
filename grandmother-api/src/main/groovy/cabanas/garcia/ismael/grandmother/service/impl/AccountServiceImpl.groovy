package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.*
import cabanas.garcia.ismael.grandmother.domain.account.repository.AccountRepository
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


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
    @Transactional
    Account payment(Long accountId, String chargeTypeId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)
        
        PaymentType chargeType = PaymentType.builder().id(chargeTypeId).build()
        Payment charge = Payment.builder().type(chargeType).amount(amount).date(date).build()
        account.charge(charge)

        accountRepository.save(account)

        return account
    }

    @Override
    @Transactional
    Account payment(Long accountId, Payment payment) {
        Account account = accountRepository.findOne(accountId)

        account.charge(payment)

        accountRepository.save(account)

        return account
    }

    @Override
    @Transactional
    Account deposit(Long accountId, String personId, BigDecimal amount, Date date) {
        Account account = accountRepository.findOne(accountId)

        Person person = Person.builder().id(personId).build()
        Deposit deposit = Deposit.builder().amount(amount).person(person).date(date).build()
        account.deposit(deposit)

        accountRepository.save(account)

        return account
    }

    @Override
    @Transactional
    Account deposit(Long accountId, Deposit deposit) {
        Account account = accountRepository.findOne(accountId)

        account.deposit(deposit)

        accountRepository.save(account)

        return account
    }

    @Override
    @Transactional
    Account open(String accountNumber) {
        open(accountNumber, BigDecimal.ZERO)
    }

    @Override
    @Transactional
    Account open(String accountNumber, BigDecimal balance) {
        Account account = Account.open(accountNumber, balance)

        accountRepository.save(account)

        log.debug("Account persited $account")

        return account
    }

    @Override
    @Transactional(readOnly = true)
    Account get(Long accountId) {
        log.debug("Getting account $accountId")

        Account account = accountRepository.findOne(accountId)

        log.debug("Account got $account")

        return account
    }

    @Override
    @Transactional(readOnly = true)
    Collection<DepositTransaction> getDepositTransactions(Long accountId) {
        return depositTransactionRepository.findByAccountIdOrderByTransactionDateAsc(accountId)
    }

    @Override
    @Transactional(readOnly = true)
    Collection<DepositTransaction> getDepositTransactionsByPersonId(Long accountId, Long personId) {
        return depositTransactionRepository.findByAccountIdAndPersonIdOrderByTransactionDateAsc(accountId, personId)
    }

    @Override
    Collection<DepositTransaction> getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year) {
        Date startDate = DateUtils.firstDateOfYear(year)

        Date endDate = DateUtils.lastDayOfYear(year)

        return depositTransactionRepository.findByAccountIdAndPersonIdAndTransactionDateBetweenOrderByTransactionDateAsc(accountId, personId, startDate, endDate)
    }


}
