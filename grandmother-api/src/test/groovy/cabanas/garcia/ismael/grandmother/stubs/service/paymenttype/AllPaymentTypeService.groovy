package cabanas.garcia.ismael.grandmother.stubs.service.paymenttype

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService

/**
 * Created by XI317311 on 02/01/2017.
 */
class AllPaymentTypeService implements PaymentTypeService{
    List<PaymentType> paymentTypes

    AllPaymentTypeService(List<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes
    }

    @Override
    def create(PaymentType chargeType) {
        return null
    }

    @Override
    def deleteAll() {
        return null
    }

    @Override
    List<PaymentType> findAll() {
        return paymentTypes.sort {it.name}
    }
}
