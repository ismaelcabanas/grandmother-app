package cabanas.garcia.ismael.grandmother.stubs.service.paymenttype

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService

import java.util.function.Predicate

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

    @Override
    Optional<PaymentType> findById(Long id) {
        Predicate<PaymentType> filterById = {paymentType -> id.equals(paymentType.id)}

        Optional<PaymentType> paymentType = paymentTypes.stream().filter(filterById).findFirst()

        return paymentType
    }
}
