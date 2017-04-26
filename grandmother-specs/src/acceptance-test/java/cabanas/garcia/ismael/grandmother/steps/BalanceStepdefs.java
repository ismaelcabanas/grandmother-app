package cabanas.garcia.ismael.grandmother.steps;

import cabanas.garcia.ismael.grandmother.model.Account;
import cabanas.garcia.ismael.grandmother.model.Deposit;
import cabanas.garcia.ismael.grandmother.model.Payment;
import cabanas.garcia.ismael.grandmother.util.AccountRestUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by XI317311 on 25/04/2017.
 */
public class BalanceStepdefs {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceStepdefs.class);

    private Account account;
    private String balanceActual;

    @Given("^que existe una cuenta en el sistema$")
    public void createAccount() throws Throwable {
        account = AccountRestUtil.createAnAccount();
    }

    @And("^la cuenta tiene los siguientes gastos$")
    public void createAccountPayments(List<Payment> paymentsDone) throws Throwable {
        AccountRestUtil.payments(account, paymentsDone);
    }

    @And("^la cuenta tiene los siguientes ingresos$")
    public void createAccountDeposits(List<Deposit> depositsDone) throws Throwable {
        AccountRestUtil.deposits(account, depositsDone);
    }

    @When("^consulto el balance a (.*) del (\\d+)$")
    public void getBalanceByMonthAndYear(String month, int year) throws Throwable {
        balanceActual = AccountRestUtil.balance(account, month, year);
    }

    @Then("^el balance es (.*)$")
    public void theBalanceIs(String balanceExpected) throws Throwable {
        Assert.assertThat(balanceActual, Is.is(IsEqual.equalTo(balanceExpected)));
    }
}
