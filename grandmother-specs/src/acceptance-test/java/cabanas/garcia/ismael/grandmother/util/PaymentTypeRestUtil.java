package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.PaymentType;
import cabanas.garcia.ismael.grandmother.util.http.HttpUtil;
import cabanas.garcia.ismael.grandmother.util.http.Response;

import java.util.*;

/**
 * Created by XI317311 on 16/02/2017.
 */
public final class PaymentTypeRestUtil {

    private static final String PAYMENT_TYPES_ENDPOINT = "http://localhost:8080/paymentTypes";

    private static Map<String, Optional<PaymentType>> paymentTypes = new HashMap<>();

    private PaymentTypeRestUtil() {
    }

    public static Optional<PaymentType> getPaymentType(String name) {
        if(paymentTypes.containsKey(name))
            return paymentTypes.get(name);

        Optional<PaymentType> paymentType = createPaymentType(name);

        paymentType.ifPresent(p -> paymentTypes.put(name, Optional.of(p)));

        return paymentType;
    }

    private static Optional<PaymentType> createPaymentType(String name) {
        HttpUtil httpUtil = new HttpUtil(PAYMENT_TYPES_ENDPOINT, PaymentType.class);

        PaymentType paymentTypePayload = PaymentType.builder().name(name).build();

        Response postResponse = httpUtil.post(paymentTypePayload);

        final Response[] responseForGetRequest = new Response[1];
        Optional<cabanas.garcia.ismael.grandmother.util.http.Header> locationHeader = postResponse.getHeader("Location");
        locationHeader.ifPresent(header -> {
            HttpUtil<PaymentType> httpUtil1 = new HttpUtil(header.getValue(), PaymentType.class);
            responseForGetRequest[0] = httpUtil1.get();
        });

        Response response = responseForGetRequest[0];

        return response.getContent();
    }
}
