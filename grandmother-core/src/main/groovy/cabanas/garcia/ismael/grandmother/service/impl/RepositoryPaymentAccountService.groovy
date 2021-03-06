package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTransactionRepository
import cabanas.garcia.ismael.grandmother.service.PaymentAccountService
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 17/01/2017.
 */
@Slf4j
@Service
class RepositoryPaymentAccountService implements PaymentAccountService {

    @Autowired
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

    @Override
    Transactions getPaymentTransactionsByYear(Long accountId, int year) {
        log.debug("Init method getPaymentTransactionsByYear($accountId, $year)")

        Date startDate = DateUtils.firstDateOfYear(year)
        log.debug("Start date $startDate")

        // last day of year
        Date endDate = DateUtils.lastDayOfYear(year)
        log.debug("End date $endDate")

        Collection<PaymentTransaction> paymentTransactions =
                paymentTransactionRepository.findByAccountIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(accountId, startDate, endDate)

        Transactions transactions = new Transactions()
        paymentTransactions.each {PaymentTransaction pt -> transactions.add(pt)}
        log.debug("Transactions $transactions")

        transactions
    }
}
