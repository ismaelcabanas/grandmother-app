package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 02/01/2017.
 */
class PaymentTypeTestUtil {
    private PaymentTypeTestUtil(){}

    static PaymentType getGasPayment() {
        PaymentType.builder().name("Gas Natural").build()
    }

    static PaymentType getEndesaPayment() {
        PaymentType.builder().name("Endesa Electricidad").build()
    }

    static PaymentType getAguaPayment() {
        return PaymentType.builder().name("Agua").build()
    }
}
