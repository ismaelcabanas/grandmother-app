package cabanas.garcia.ismael.grandmother.steps;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Payment;
import cabanas.garcia.ismael.grandmother.model.PaymentType;
import cabanas.garcia.ismael.grandmother.util.AccountRestUtil;
import cabanas.garcia.ismael.grandmother.util.PaymentTypeRestUtil;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by XI317311 on 15/02/2017.
 */
public class PaymentsPerYearStepDefs {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsPerYearStepDefs.class);

    private static final String APPLICATION_JSON = "application/json";
    private Account account;

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
    public void consultoLosPagosDelAño(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^los pagos realizados en el año son$")
    public void losPagosRealizadosEnElAñoSon() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^la cantidad total de los pagos del año es (\\d+)$")
    public void laCantidadTotalDeLosPagosDelAñoEs(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^no hay pagos realizados en el año$")
    public void noHayPagosRealizadosEnElAño() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
