package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 09/12/2016.
 */
interface ChargeTypeService {

    def create(PaymentType chargeType)

    def deleteAll()

    List<PaymentType> findAll()
}