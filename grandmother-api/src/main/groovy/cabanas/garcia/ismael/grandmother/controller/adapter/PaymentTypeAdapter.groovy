package cabanas.garcia.ismael.grandmother.controller.adapter

import cabanas.garcia.ismael.grandmother.controller.response.PaymentTypeResponse
import cabanas.garcia.ismael.grandmother.controller.response.PaymentTypesResponse
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 03/01/2017.
 */
final class PaymentTypeAdapter {

    static PaymentTypesResponse mapEntitiesToResponse(List<PaymentType> paymentTypes) {
        Collection<PaymentTypeResponse> paymentTypeResponseList = new ArrayList<>()

        paymentTypes.each {paymentType -> paymentTypeResponseList.add(mapEntityToResponse(paymentType))}

        PaymentTypesResponse paymentTypesResponse = PaymentTypesResponse.builder()
                .paymentTypes(paymentTypeResponseList)
                .build()

        paymentTypesResponse
    }

    private PaymentTypeAdapter() {
    }

    static PaymentTypeResponse mapEntityToResponse(PaymentType paymentType) {
        PaymentTypeResponse paymentTypeResponse = PaymentTypeResponse.builder()
                .name(paymentType.name)
                .id(paymentType.id)
                .build()
        paymentTypeResponse
    }
}
