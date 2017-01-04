package cabanas.garcia.ismael.grandmother.utils

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 02/01/2017.
 */
final class PaymentTypeTestUtil {
    private PaymentTypeTestUtil(){}

    static PaymentType getGasPayment() {
        PaymentType.builder().id(1).name("Gas Natural").build()
    }

    static PaymentType getEndesaPayment() {
        PaymentType.builder().id(2).name("Endesa Electricidad").build()
    }

    static PaymentType getAguaPayment() {
        return PaymentType.builder().id(3).name("Agua").build()
    }
}
