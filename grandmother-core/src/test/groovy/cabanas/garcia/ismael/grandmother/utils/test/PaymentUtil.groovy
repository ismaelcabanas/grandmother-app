package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.Payment

final class PaymentUtil {

    static Payment paymentForWaterOf20000Yesterday() {
        Payment.builder()
            .amount(AmountUtil.TWENTY_THOUSAND)
            .type(PaymentTypeUtil.WATER_PAYMENT)
            .date(DateUtil.YESTERDAY)
            .description("Payment for water")
            .build()
    }
}
