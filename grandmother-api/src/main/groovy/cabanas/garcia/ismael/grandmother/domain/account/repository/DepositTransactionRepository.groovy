package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.DepositTransaction
import org.springframework.data.repository.CrudRepository

/**
 * Created by XI317311 on 23/12/2016.
 */
interface DepositTransactionRepository extends CrudRepository<DepositTransaction, Long>{
    Collection<DepositTransaction> findByAccountIdOrderByDateOfMovementAsc(Long accountId)

    Collection<DepositTransaction> findByAccountIdAndPersonIdOrderByDateOfMovementAsc(Long accountId, Long personId)
}