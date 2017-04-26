package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.PaymentType;
import cabanas.garcia.ismael.grandmother.util.http.Response;

import java.util.HashMap;
import java.util.Map;

public final class PaymentTypeRestUtil {

    private static final String PAYMENT_TYPES_ENDPOINT = "http://localhost:8080/paymentTypes";
    private static final Class<PaymentType> TARGET_CLASS = PaymentType.class;

    private static Map<String, PaymentType> paymentTypes = new HashMap<>();

    private PaymentTypeRestUtil() {
    }

    public static PaymentType getPaymentType(String name) {
        if(paymentTypes.containsKey(name))
            return paymentTypes.get(name);

        PaymentType newPaymentType = createPaymentType(name);
        paymentTypes.put(newPaymentType.getName(), newPaymentType);
        return newPaymentType;
    }

    private static PaymentType createPaymentType(String name) {
        PaymentType paymentTypePayload = PaymentType.builder().name(name).build();

        Response postAndGetResponse = RestUtil.postAndGet(PAYMENT_TYPES_ENDPOINT, paymentTypePayload, TARGET_CLASS);

        return (PaymentType) postAndGetResponse.getContent().get();
    }
}
