package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.PaymentTypeRepository
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 09/12/2016.
 */
@Service
class RepositoryPaymentTypeService implements PaymentTypeService{

    @Autowired
    PaymentTypeRepository paymentTypeRepository

    @Override
    def create(PaymentType chargeType) {
        paymentTypeRepository.save(chargeType)
    }

    @Override
    List<PaymentType> findAll() {
        paymentTypeRepository.findAllByOrderByNameAsc()
    }

    @Override
    PaymentType findById(Long id) {
        return null
    }

    @Override
    def deleteAll() {
        paymentTypeRepository.deleteAll()
    }
}
