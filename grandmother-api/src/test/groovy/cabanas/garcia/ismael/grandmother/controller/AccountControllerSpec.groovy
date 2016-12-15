package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.ChargeMovement
import cabanas.garcia.ismael.grandmother.domain.account.DepositMovement
import cabanas.garcia.ismael.grandmother.domain.account.Transactions
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountControllerSpec extends Specification{

    static Date TODAY = new Date()
    static Date YESTERDAY = new Date().previous()

    def "should return status 200 when hits the URL for getting an existing account"(){
        given: "a given account identifier"
            String accountId = "1"
        and: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$accountId")
        then:
            response.status == HttpStatus.OK
    }

    def "should get account details when hits the URL for getting an existing account"(){
        given: "a given account with movements"
            Transactions transactions = new Transactions()
            transactions.add(new DepositMovement(amount: 10000, dateOfMovement: TODAY))
            transactions.add(new DepositMovement(amount: 15000, dateOfMovement: YESTERDAY))
            transactions.add(new ChargeMovement(amount: 10000, dateOfMovement: TODAY))
            Account account = Account.builder()
                .balance(30000).accountNumber("123").id("1").transactions(transactions).build()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == 30000
            jsonResponse.accountNumber == "123"
    }

    def accountDetails() {
        new Account()
    }

    def sendGet(controller, path) {
        MockMvc mockMvc = standaloneSetup(controller).build()
        def response = mockMvc.perform(
                get(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)

                )
                .andDo(log())
                .andReturn().response

        return response
    }
}
