package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 02/01/2017.
 */
final class PaymentTypeUtil {
    public static PaymentType WATER_PAYMENT = PaymentType.builder().name("Agua").build()
    public static PaymentType GAS_PAYMENT = PaymentType.builder().name("Gas Natural").build()
    public static PaymentType ENDESA_PAYMENT = PaymentType.builder().name("Endesa Electricidad").build()

    private PaymentTypeUtil(){}

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
