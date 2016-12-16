package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.service.ChargeTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by XI317311 on 09/12/2016.
 */
@Service
class ChargeTypeServiceImpl implements ChargeTypeService{

    @Autowired
    ChargeTypeRepository chargeTypeRepository

    @Override
    def create(PaymentType chargeType) {
        chargeTypeRepository.save(chargeType)
    }

    @Override
    List<PaymentType> findAll() {
        chargeTypeRepository.findAll()
    }

    @Override
    def deleteAll() {
        chargeTypeRepository.deleteAll()
    }
}
