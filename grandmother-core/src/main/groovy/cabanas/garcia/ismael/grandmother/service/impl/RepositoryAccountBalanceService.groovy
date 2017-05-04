package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import cabanas.garcia.ismael.grandmother.domain.account.repository.TransactionRepository
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.stereotype.Service

import java.util.function.BinaryOperator
import java.util.function.Function

@Slf4j
@Service
class RepositoryAccountBalanceService implements AccountBalanceService{

    private static final BigDecimal ZERO_BALANCE = BigDecimal.ZERO

    TransactionRepository transactionRepository

    RepositoryAccountBalanceService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository
    }

    @Override
    BigDecimal balance(long accountId, int year, int month) {
        log.debug("Init method balance($accountId, $year, $month)")

        BigDecimal balance = ZERO_BALANCE

        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0)

        // last day of month
        DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
        Date endDate = lastDayOfMonth.toDate()
        log.debug("End date $endDate")

        Collection<Transaction> transactions = transactionRepository.findByAccountIdAndDateOfMovementLessThan(accountId, endDate)

        BinaryOperator<BigDecimal> addOperator = { s1, s2 -> s1.add(s2)}

        BigDecimal depositsBalance = transactions.stream()
                                        .filter {transaction -> transaction instanceof DepositTransaction}
                                        .map {transaction -> transaction.amount}
                                        .reduce(ZERO_BALANCE, addOperator)

        BigDecimal paymentsBalance = transactions.stream()
                .filter {t -> t instanceof PaymentTransaction}
                .map {transaction -> transaction.amount}
                .reduce(ZERO_BALANCE, addOperator)

        return depositsBalance.add(paymentsBalance.negate())
    }
}
