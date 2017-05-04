package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import org.springframework.data.repository.Repository

interface TransactionRepository extends Repository<Transaction, Long>{

    //@Query("select sum(t.amount) from #{#entityName} t where t.account.id=:accountId and t.dateOfMovement <= :endDate")
    //BigDecimal balance(@Param("accountId") long accountId, @Param("endDate") Date endDate)
    Collection<Transaction> findByAccountIdAndDateOfMovementLessThan(Long accountId, Date endDate)
}