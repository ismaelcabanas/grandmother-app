package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.*;
import cabanas.garcia.ismael.grandmother.util.http.HttpUtil;
import cabanas.garcia.ismael.grandmother.util.http.Response;
import cabanas.garcia.ismael.grandmother.util.http.payload.DepositPayload;
import cabanas.garcia.ismael.grandmother.util.http.payload.PaymentPayload;
import cabanas.garcia.ismael.grandmother.util.http.response.BalanceResponse;
import cabanas.garcia.ismael.grandmother.util.http.response.PaymentsResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by XI317311 on 16/02/2017.
 */
public final class AccountRestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestUtil.class);

    public static final String ACCOUNTS_ENDPOINT = "http://localhost:8080/accounts";
    public static final String ACCOUNTS_PAYMENT_ENDPOINT = "http://localhost:8080/accounts/{id}/payment";
    public static final String ACCOUNTS_DEPOSIT_ENDPOINT = "http://localhost:8080/accounts/{id}/deposit";
    private static final String ACCOUNTS_PAYMENTS_ENDPOINT = "http://localhost:8080/accounts/{id}/payments";
    private static final String ACCOUNTS_BALANCE_ENDPOINT = "http://localhost:8080/accounts/{id}/balance";

    private static final Class<Account> TARGET_CLASS = Account.class;

    private AccountRestUtil(){

    }

    /**
     * Call GrandMother API for creating an account with 0 balance.
     *
     * @return
     */
    public static Account createAnAccount() {
        String accountRandomNumber = "ES" + RandomStringUtils.randomNumeric(22);
        return createAnAccount(accountRandomNumber);
    }

    public static Account createAnAccount(String accountNumber) {
        Account accountPayload = Account.builder().accountNumber(accountNumber).balance(BigDecimal.ZERO).build();

        Response postAndGetResponse = RestUtil.postAndGet(ACCOUNTS_ENDPOINT, accountPayload, TARGET_CLASS);

        return (Account) postAndGetResponse.getContent().get();
    }

    public static void payment(Long accountId, PaymentType paymentType, String amount, String paymentDate) {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .amount(amount)
                .dateOfPayment(paymentDate)
                .paymentTypeId(paymentType.getId())
                .description("")
                .build();

        HttpUtil httpUtil = new HttpUtil(ACCOUNTS_PAYMENT_ENDPOINT, Void.class);

        httpUtil.addPathVariable("id", String.valueOf(accountId));

        httpUtil.put(paymentPayload);
    }

    public static PaymentsResponse paymentsPerYear(Long accountId, int year) {
        HttpUtil<PaymentsResponse> httpUtil = new HttpUtil(ACCOUNTS_PAYMENTS_ENDPOINT, PaymentsResponse.class);

        httpUtil.addPathVariable("id", String.valueOf(accountId));
        httpUtil.addQueryString("year", String.valueOf(year));

        Response response = httpUtil.get();

        return (PaymentsResponse) response.getContent().get();
    }

    public static void payments(final Account account, final List<Payment> paymentsDone) {
        paymentsDone.forEach(paymentDone -> {
            PaymentType paymentType = PaymentTypeRestUtil.getPaymentType(paymentDone.getPaymentType());
            payment(account.getId(), paymentType, paymentDone.getAmount(), paymentDone.getDate());
        });
    }

    public static void deposits(final Account account, final List<Deposit> depositsDone) {
        depositsDone.forEach(depositDone -> {
            Person person = PersonRestUtil.getPerson(depositDone.getPerson());
            deposit(account.getId(), person, depositDone.getAmount(), depositDone.getDate());
        });
    }

    private static void deposit(Long accountId, Person person, String amount, String date) {
        DepositPayload depositPayload = DepositPayload.builder()
                .deposit(amount)
                .dateOfDeposit(date)
                .personId(person.getId())
                .description("")
                .build();

        HttpUtil httpUtil = new HttpUtil(ACCOUNTS_DEPOSIT_ENDPOINT, Void.class);

        httpUtil.addPathVariable("id", String.valueOf(accountId));

        httpUtil.put(depositPayload);
    }

    public static String balance(final Account account, String month, int year) {
        HttpUtil httpUtil = new HttpUtil(ACCOUNTS_BALANCE_ENDPOINT, BalanceResponse.class);

        httpUtil.addPathVariable("id", String.valueOf(account.getId()));

        httpUtil.addQueryString("month", monthPositionOf(month));
        httpUtil.addQueryString("year", String.valueOf(year));

        Response response = httpUtil.get();

        BalanceResponse balanceResponse = (BalanceResponse) response.getContent().get();

        return balanceResponse.getBalance();
    }

    private static String monthPositionOf(String month) {
        Optional<MonthEnum> opMonthEnum = MonthEnum.monthOf(month);
        if(opMonthEnum.isPresent()){
            return String.valueOf(opMonthEnum.get().getPosition());
        }
        return "";
    }
}
