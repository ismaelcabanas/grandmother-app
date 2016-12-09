package cabanas.garcia.ismael.grandmother.service.impl

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.account.repository.ChargeTypeRepository
import cabanas.garcia.ismael.grandmother.service.ChargeTypeService

/**
 * Created by XI317311 on 09/12/2016.
 */
class ChargeTypeServiceImpl implements ChargeTypeService{

    ChargeTypeRepository chargeTypeRepository

    @Override
    def create(ChargeType chargeType) {
        chargeTypeRepository.save(chargeType)
    }

    @Override
    List<ChargeType> findAll() {
        chargeTypeRepository.findAll()
    }

    @Override
    def deleteAll() {
        chargeTypeRepository.deleteAll()
    }
}
