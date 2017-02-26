package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Payment;
import cabanas.garcia.ismael.grandmother.util.http.HttpUtil;
import cabanas.garcia.ismael.grandmother.util.http.Response;
import cabanas.garcia.ismael.grandmother.util.http.payload.PaymentPayload;
import cabanas.garcia.ismael.grandmother.util.http.response.PaymentsResponse;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by XI317311 on 16/02/2017.
 */
public final class AccountRestUtil {

    public static final String ACCOUNTS_ENDPOINT = "http://localhost:8080/accounts";
    public static final String PAYMENT_ENDPOINT = "http://localhost:8080/accounts/{id}/payment";
    private static final String PAYMENTS_ACCOUNT_ENDPOINT = "http://localhost:8080/accounts/{id}/payments";

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

        HttpUtil<Account> httpUtil = new HttpUtil(ACCOUNTS_ENDPOINT, Account.class);
        Response postResponse = httpUtil.post(accountPayload);

        final Response[] responseForGetRequest = new Response[1];
        Optional<cabanas.garcia.ismael.grandmother.util.http.Header> locationHeader = postResponse.getHeader("Location");
        locationHeader.ifPresent(header -> {
            HttpUtil<Account> httpUtil1 = new HttpUtil(header.getValue(), Account.class);
            responseForGetRequest[0] = httpUtil1.get();
        });

        Response response = responseForGetRequest[0];

        return (Account) response.getContent().get();
    }

    public static void payment(Long accountId, Long paymentTypeId, String amount, String paymentDate) {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .amount(amount)
                .dateOfPayment(paymentDate)
                .paymentTypeId(paymentTypeId)
                .description("")
                .build();

        HttpUtil httpUtil = new HttpUtil(PAYMENT_ENDPOINT, Void.class);

        httpUtil.addPathVariable("id", String.valueOf(accountId));

        httpUtil.put(paymentPayload);
    }

    public static PaymentsResponse paymentsPerYear(Long accountId, int year) {
        HttpUtil<PaymentsResponse> httpUtil = new HttpUtil(PAYMENTS_ACCOUNT_ENDPOINT, PaymentsResponse.class);

        httpUtil.addPathVariable("id", String.valueOf(accountId));
        httpUtil.addQueryString("year", String.valueOf(year));

        Response response = httpUtil.get();

        return (PaymentsResponse) response.getContent().get();
    }
}
