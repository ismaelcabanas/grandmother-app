package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.repository.TransactionRepository
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.stereotype.Service

@Slf4j
@Service
class RepositoryAccountBalanceService implements AccountBalanceService{
    TransactionRepository transactionRepository

    RepositoryAccountBalanceService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository
    }

    @Override
    BigDecimal balance(long accountId, int year, int month) {
        log.debug("Init method balance($accountId, $year, $month)")

        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0)

        Date startDate = dateTime.toDate()
        log.debug("Start date $startDate")

        // last day of month
        DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
        Date endDate = lastDayOfMonth.toDate()
        log.debug("End date $endDate")

        return transactionRepository.balance(accountId, startDate, endDate)
    }
}
