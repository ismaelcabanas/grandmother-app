package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.domain.account.repository.DepositTransactionRepository
import cabanas.garcia.ismael.grandmother.service.DepositAccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 05/01/2017.
 */
@Slf4j
@Service
class RepositoryDepositAccountService implements DepositAccountService {

    @Autowired
    DepositTransactionRepository depositTransactionRepository

    @Override
    Transactions getDepositTransactions(Long accountId) {
        log.debug("getDepositTransactions($accountId)")

        Transactions transactions = new Transactions()

        Collection<DepositTransaction> depositsTransactions =
                depositTransactionRepository.findByAccountIdOrderByDateOfMovementAsc(accountId)

        log.debug("Deposit transactions in repository: $depositsTransactions")

        depositsTransactions.each {DepositTransaction dt -> transactions.add(dt)}

        transactions
    }

    @Override
    Transactions getDepositTransactionsByPersonId(Long accountId, Long personId) {
        return null
    }

    @Override
    Transactions getDepositTransactionsByPersonIdAndYear(Long accountId, Long personId, int year) {
        return null
    }
}
