package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType

/**
 * Created by XI317311 on 02/01/2017.
 */
final class PaymentTypeUtil {
    public static PaymentType WATER_PAYMENT = PaymentType.builder().name(PAYMENT_WATER_NAME).build()
    public static PaymentType GAS_PAYMENT = PaymentType.builder().name("Gas Natural").build()
    public static PaymentType ENDESA_PAYMENT = PaymentType.builder().name("Endesa Electricidad").build()
    public static final String PAYMENT_WATER_NAME = "Agua"

    private PaymentTypeUtil(){}

    static PaymentType getGasPersistedPayment() {
        PaymentType.builder().id(1).name("Gas Natural").build()
    }

    static PaymentType getEndesaPersistedPayment() {
        PaymentType.builder().id(2).name("Endesa Electricidad").build()
    }

    static PaymentType getAguaPersistedPayment() {
        return PaymentType.builder().id(3).name(PAYMENT_WATER_NAME).build()
    }

    static PaymentType getWaterPayment(){
        PaymentType.builder().name(PAYMENT_WATER_NAME).build()
    }
}
