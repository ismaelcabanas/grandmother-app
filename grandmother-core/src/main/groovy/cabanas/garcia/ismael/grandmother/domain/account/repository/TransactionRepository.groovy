package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.Transaction
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

interface TransactionRepository extends Repository<Transaction, Long>{

    @Query("select sum(t.amount) from #{#entityName} t where t.account.id=:accountId and t.dateOfMovement <= :endDate")
    BigDecimal balance(@Param("accountId") long accountId, @Param("endDate") Date endDate)
}