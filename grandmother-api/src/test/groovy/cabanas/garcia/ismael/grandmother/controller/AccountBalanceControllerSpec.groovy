package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.service.AccountBalanceService
import cabanas.garcia.ismael.grandmother.stubs.service.account.AccountBalanceServiceDefaultStub
import cabanas.garcia.ismael.grandmother.stubs.service.account.AccountBalanceServiceNonExistAccountStub
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
import cabanas.garcia.ismael.grandmother.utils.test.PaymentUtil
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class AccountBalanceControllerSpec extends Specification{

    def "should return status 200 when hits the URL for getting the balance for an existing account for a given month and year"(){
        given: "a given account"
            Account account = AccountUtil.getDefaultAccountPersisted()
        and: "a given month and year"
            String month = 2
            String year = 2010
        and: "balance controller configured with stub account service"
            AccountBalanceService balanceService = new AccountBalanceServiceDefaultStub(account)
            AccountBalanceController sut = new AccountBalanceController(balanceService)
        when: "REST balance get url is hit"
            def response = sendGet(sut, "/accounts/$account.id/balance?year=$year&month=$month")
        then:
            responseStatusCodeIsOk(response)
            responseContentTypeIsJson(response)
    }

    def "should use balance service for getting balance from an account"(){
        given: "a given account"
            Account account = AccountUtil.getDefaultAccountPersisted()
        and: "a given month and year"
            int month = 2
            int year = 2010
        and: "balance controller configured with stub account service"
            AccountBalanceService balanceService = Mock(AccountBalanceService)
            AccountBalanceController sut = new AccountBalanceController(balanceService)
        when: "REST balance get url is hit"
            def response = sendGet(sut, "/accounts/$account.id/balance?year=$year&month=$month")
        then:
            1 * balanceService.balance(account.getId().longValue(), year, month)
    }

    def "should return balance when hits the URL for getting the balance for an existing account for a given month and year"(){
        given: "a given account"
            Account account = AccountUtil.getDefaultAccountPersisted()
            BigDecimal balanceExpected = account.balance
            account.deposit(DepositUtil.depositFromIsmaelOf10000Today())
            account.charge(PaymentUtil.paymentForWaterOf20000Yesterday())
            balanceExpected += AmountUtil.TEN_THOUSAND - AmountUtil.TWENTY_THOUSAND
        and: "a given month and year"
            String month = 2
            String year = 2010
        and: "balance controller configured with stub account service"
            AccountBalanceService balanceService = new AccountBalanceServiceDefaultStub(account)
            AccountBalanceController sut = new AccountBalanceController(balanceService)
        when: "REST balance get url is hit"
            def response = sendGet(sut, "/accounts/$account.id/balance?year=$year&month=$month")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == balanceExpected
    }

    def "should return zero balance when hits the URL for getting the balance for an inexisting account"(){
        given: "a given account"
            long notExistAccountId = 1L
            BigDecimal balanceExpected = AmountUtil.ZERO
        and: "a given month and year"
            String month = 2
            String year = 2010
        and: "balance controller configured with stub account service"
            AccountBalanceService balanceService = new AccountBalanceServiceDefaultStub()
            AccountBalanceController sut = new AccountBalanceController(balanceService)
        when: "REST balance get url is hit"
        def response = sendGet(sut, "/accounts/$notExistAccountId/balance?year=$year&month=$month")
        then:
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        jsonResponse.balance == balanceExpected
    }

    def "should return zero balance when hits the URL for getting the balance for an existing account but there is not transactions for given month and year"(){
        given: "a given account"
            Account account = AccountUtil.getDefaultAccountPersisted()
            BigDecimal balanceExpected = account.balance
        and: "a given month and year"
            String month = 2
            String year = 2010
        and: "balance controller configured with stub account service"
            AccountBalanceService balanceService = new AccountBalanceServiceDefaultStub(account)
            AccountBalanceController sut = new AccountBalanceController(balanceService)
        when: "REST balance get url is hit"
            def response = sendGet(sut, "/accounts/$account.id/balance?year=$year&month=$month")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == balanceExpected
    }

    def responseStatusCodeIsNotFound(MockHttpServletResponse response) {
        response.status == HttpStatus.NOT_FOUND.value()
    }

    def responseStatusCodeIsOk(MockHttpServletResponse response) {
        response.status == HttpStatus.OK.value()
    }

    def responseContentTypeIsJson(MockHttpServletResponse response) {
        response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
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
