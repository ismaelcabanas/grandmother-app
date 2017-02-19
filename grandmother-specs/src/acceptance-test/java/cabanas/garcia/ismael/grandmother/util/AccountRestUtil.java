package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Payment;
import cabanas.garcia.ismael.grandmother.util.http.HttpUtil;
import cabanas.garcia.ismael.grandmother.util.http.Response;
import cabanas.garcia.ismael.grandmother.util.http.payload.PaymentPayload;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by XI317311 on 16/02/2017.
 */
public final class AccountRestUtil {

    public static final String ACCOUNTS_ENDPOINT = "http://localhost:8080/accounts";
    public static final String PAYMENT_ENDPOINT = "http://localhost:8080/accounts/{id}/payment";

    private AccountRestUtil(){

    }

    /**
     * Call GrandMother API for creating an account with 0 balance.
     *
     * @return
     */
    public static Account createAnAccount() {
        String accountRandomNumber = "ES" + RandomStringUtils.randomNumeric(22);
        Account accountPayload = Account.builder().accountNumber(accountRandomNumber).balance(BigDecimal.ZERO).build();

        HttpUtil httpUtil = new HttpUtil(ACCOUNTS_ENDPOINT, Account.class);
        Response postResponse = httpUtil.post(accountPayload);

        final Response[] responseForGetRequest = new Response[1];
        Optional<cabanas.garcia.ismael.grandmother.util.http.Header> locationHeader = postResponse.getHeader("Location");
        locationHeader.ifPresent(header -> {
            responseForGetRequest[0] = httpUtil.get(header.getValue());
        });

        Response response = responseForGetRequest[0];

        return (Account) response.getContent();
    }

    public static void payment(Long accountId, Long paymentTypeId, BigDecimal amount, String paymentDate) {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .amount(amount)
                .dateOfPayment(paymentDate)
                .paymentTypeId(paymentTypeId)
                .description("")
                .build();

        String endpoint = PAYMENT_ENDPOINT.replace("{id}", String.valueOf(accountId));
        HttpUtil httpUtil = new HttpUtil(endpoint);

        httpUtil.put(paymentPayload);
    }
}
