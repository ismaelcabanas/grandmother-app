package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Charge
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
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
            Account account = AccountTestUtils.getDefaultAccount()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            response.status == HttpStatus.OK.value()
    }

    def "should get account details when hits the URL for getting an existing account"(){
        given: "a given account with transactions"
            Account account = AccountTestUtils.getDefaultAccount()
            account.deposit(new Deposit(amount: 10000, date: TODAY))
            account.deposit(new Deposit(amount: 15000, date: YESTERDAY))
            account.charge(new Charge(amount: 10000, date: TODAY))
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == AccountTestUtils.getDefaultAccount().balance.add(15000)
            jsonResponse.accountNumber == AccountTestUtils.getDefaultAccount().accountNumber
            jsonResponse.id == AccountTestUtils.getDefaultAccount().id
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
