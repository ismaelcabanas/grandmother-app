package cabanas.garcia.ismael.grandmother.domain.account.repository

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import org.springframework.data.repository.CrudRepository

/**
 * Created by XI317311 on 09/12/2016.
 */
interface PaymentTypeRepository extends CrudRepository<PaymentType, Long>{

    Collection<PaymentType> findAllByOrderByNameAsc()
}