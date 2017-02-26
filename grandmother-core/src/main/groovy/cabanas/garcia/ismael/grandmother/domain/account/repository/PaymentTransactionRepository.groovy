package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.PaymentTransaction
import org.springframework.data.repository.CrudRepository

/**
 * Created by XI317311 on 17/01/2017.
 */
interface PaymentTransactionRepository extends CrudRepository<PaymentTransaction, Long>{

    Collection<PaymentTransaction> findByAccountIdAndDateOfMovementBetweenOrderByDateOfMovementAsc(Long accountId, Date startDate, Date endDate)
}