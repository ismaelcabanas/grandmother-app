package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTransactionRepository
import cabanas.garcia.ismael.grandmother.service.PaymentAccountService
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.joda.time.Months

/**
 * Created by XI317311 on 17/01/2017.
 */
@Slf4j
class RepositoryPaymentAccountService implements PaymentAccountService {
    PaymentTransactionRepository paymentTransactionRepository

    @Override
    Transactions getPaymentTransactionsByYearAndMonth(Long accountId, int year, int month) {
        log.debug("Init method getPaymentTransactionsByYearAndMonth($accountId, $year, $month)")

        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0)

        Date startDate = dateTime.toDate()
        log.debug("Start date $startDate")

        // last day of month
        DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
        Date endDate = lastDayOfMonth.toDate()
        log.debug("End date $endDate")

        Collection<PaymentTransaction> paymentTransactions =
                paymentTransactionRepository.findByAccountIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(accountId, startDate, endDate)

        Transactions transactions = new Transactions()
        paymentTransactions.each {PaymentTransaction pt -> transactions.add(pt)}
        log.debug("Transactions $transactions")
        
        transactions
    }
}
