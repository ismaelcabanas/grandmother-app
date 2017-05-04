package cabanas.garcia.ismael.grandmother.utils.test

import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.utils.DateUtils

final class PaymentUtil {

    static Payment paymentForWaterOf20000Yesterday() {
        Payment.builder()
            .amount(AmountUtil.TWENTY_THOUSAND)
            .type(PaymentTypeUtil.WATER_PAYMENT)
            .date(DateUtil.YESTERDAY)
            .description("Payment for water")
            .build()
    }

    static Payment paymentOf(String paymentName, BigDecimal amount, String date) {
        Payment.builder()
            .amount(amount)
            .type(PaymentTypeUtil.getPayment(paymentName))
            .date(DateUtils.parse(date))
            .description("Payment")
            .build()
    }
}
