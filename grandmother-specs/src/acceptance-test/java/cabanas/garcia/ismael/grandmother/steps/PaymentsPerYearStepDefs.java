package cabanas.garcia.ismael.grandmother.steps;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Payment;
import cabanas.garcia.ismael.grandmother.model.PaymentType;
import cabanas.garcia.ismael.grandmother.util.AccountRestUtil;
import cabanas.garcia.ismael.grandmother.util.DateUtil;
import cabanas.garcia.ismael.grandmother.util.PaymentTypeRestUtil;
import cabanas.garcia.ismael.grandmother.util.http.response.PaymentResponse;
import cabanas.garcia.ismael.grandmother.util.http.response.PaymentsResponse;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.collection.IsArrayWithSize;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by XI317311 on 15/02/2017.
 */
public class PaymentsPerYearStepDefs {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsPerYearStepDefs.class);

    private static final String APPLICATION_JSON = "application/json";
    private Account account;
    private PaymentsResponse paymentsResponse;

    @Given("^una cuenta bancaria con los pagos$")
    public void laCuentaDeMiAbuelaConLosPagos(List<Payment> payments) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        account = AccountRestUtil.createAnAccount();

        payments.forEach(payment -> {
            Optional<PaymentType> paymentType = PaymentTypeRestUtil.getPaymentType(payment.getPaymentType());
            if(paymentType.isPresent())
                AccountRestUtil.payment(account.getId(), paymentType.get().getId(), payment.getAmount(), payment.getDate());
            else
                LOGGER.debug("No existe tipo de pago %s", payment.getPaymentType());
        });
    }

    @When("^consulto los pagos del año (\\d+)$")
    public void consultoLosPagosDelAnio(int year) throws Throwable {
        paymentsResponse = AccountRestUtil.paymentsPerYear(account.getId(), year);
    }

    @Then("^los pagos realizados en el año son$")
    public void losPagosRealizadosEnElAnioSon(List<Payment> paymentsDone) throws Throwable {
        boolean result = true;
        for(Payment payment : paymentsDone){
            boolean partialResult = false;
            for (PaymentResponse paymentResponse : paymentsResponse.getPayments()){
                partialResult = (payment.getPaymentType().equals(paymentResponse.getPaymentType().getName())
                    && DateUtil.compareDates(payment.getDate(), paymentResponse.getDate()))
                    && payment.getAmount().equals(paymentResponse.getAmount());
                if(partialResult)
                    break;
            }
            if(!partialResult) {
                result = false;
                break;
            }
        }
        Assert.assertTrue(result);
    }

    @And("^la cantidad total de los pagos del año es (.*)$")
    public void laCantidadTotalDeLosPagosDelAnioEs(BigDecimal amount) throws Throwable {
        Assert.assertThat(paymentsResponse.getTotal(), Is.is(IsEqual.equalTo(amount)));
    }

    @Then("^no hay pagos realizados en el año$")
    public void noHayPagosRealizadosEnElAnio() throws Throwable {
        Assert.assertThat(paymentsResponse.getPayments(), Is.is(IsArrayWithSize.emptyArray()));
    }

}
