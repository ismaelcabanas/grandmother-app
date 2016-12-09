package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType

/**
 * Created by XI317311 on 09/12/2016.
 */
interface ChargeTypeService {

    def create(ChargeType chargeType)

    def deleteAll()

    List<ChargeType> findAll()
}