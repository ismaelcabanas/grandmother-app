package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.AccountRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.DepositRequestBody
import cabanas.garcia.ismael.grandmother.controller.request.PaymentRequestBody
import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceDefaultAccountStub
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import cabanas.garcia.ismael.grandmother.utils.test.AccountUtil
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountControllerSpec extends Specification{

    def "should return status 200 when hits the URL for getting an existing account"(){
        given: "a given account identifier"
            Account account = getDefaultAccount()
        and: "account controller configured with stub service"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            responseStatusCodeIsOk(response)
            responseContentTypeIsJson(response)
    }

    def "should get account details when hits the URL for getting an existing account"(){
        given: "a given account with list"
            Account account = getDefaultAccount()
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: TODAY)
            Deposit deposit15000 = new Deposit(amount: 15000, date: YESTERDAY)
            deposit(account, deposit10000)
            deposit(account, deposit15000)
            payment(account, AmountUtil.TEN_THOUSAND, TODAY)
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == AccountUtil.getDefaultAccountPersisted().balance.add(15000)
            jsonResponse.accountNumber == AccountUtil.getDefaultAccountPersisted().accountNumber
            jsonResponse.id == AccountUtil.getDefaultAccountPersisted().id
    }

    def "should get account details when hits the URL for getting an existing account with balance 0"(){
        given: "a given account with balance 0"
            Account account = Account.builder().id(1L).accountNumber("123").balance(0).build()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.balance == account.balance
            jsonResponse.accountNumber == account.accountNumber
            jsonResponse.id == account.id
    }

    def "should return status 201 when hits URL for creating an account"(){
        given: "account controller configured with his services"
            AccountService accountService = new AccountServiceDefaultAccountStub()
            AccountController controller = new AccountController(accountService: accountService)
        and: "a body with account data"
            AccountRequestBody accountRequestBody = AccountRequestBody.builder()
                .accountNumber(AccountUtil.DEFAULT_ACCOUNT_NUMBER)
                .build()
        when: "REST account post url is hit"
            def response = sendPost(controller, "/accounts", accountRequestBody)
        then:
            response.status == HttpStatus.CREATED.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
            response.getHeader("Location") == "http://localhost/accounts/1"
    }

    def "should return status 400 when hits URL for creating an account with empty account number"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a body with account data with empty account number"
            AccountRequestBody accountRequestBody = AccountRequestBody.builder()
                .balance(AccountUtil.DEFAULT_BALANCE)
                .build()
        when: "REST account post url is hit"
            def response = sendPost(controller, "/accounts", accountRequestBody)
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return status 204 when hits URL for depositing on account"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountUtil.getDefaultAccountPersisted()
            String accountId = defaultAccount.getId()
        and: "request body of deposit"
            DepositRequestBody depositRequestBody = DepositRequestBody.builder()
                    .personId(1)
                    .deposit(defaultAccount.balance)
                    .dateOfDeposit(TODAY)
                    .description("Transferencia a su favor")
                    .build()
        when: "REST desposit on account url is hit"
            def response = sendPut(controller, "/accounts/$accountId/deposit", depositRequestBody)
        then:
            responseStatusCodeIsNoContent(response)
            responseContentTypeIsJson(response)
    }

    @Unroll
    def "should return status #badRequestStatus when hits URL for depositing on account with invalid parameters"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountUtil.getDefaultAccountPersisted()
            String accountId = defaultAccount.getId()
        and: "data of deposit"
            DepositRequestBody depositRequestBody = new DepositRequestBody(personId: personId, deposit: amount, dateOfDeposit: date)
        when: "REST desposit on account url is hit"
            def response = sendPut(controller, "/accounts/$accountId/deposit", depositRequestBody)
        then:
            response.status == badRequestStatus
        where:
        amount | personId | date  | badRequestStatus
        null   | "1"      | TODAY | HttpStatus.BAD_REQUEST.value()
        30000  | "1"      | null  | HttpStatus.BAD_REQUEST.value()
        30000  | null     | TODAY | HttpStatus.BAD_REQUEST.value()
    }

    def "should return status 204 when hits URL for payments on account"(){
        given: "account controller configured with his services"
         AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountUtil.getDefaultAccountPersisted()
            String accountId = defaultAccount.getId()
        and: "data of payment"
            Date date = TODAY
            BigDecimal amount = defaultAccount.getBalance()
            String paymentTypeId = "1"
            String description = "Agua"
            PaymentRequestBody paymentRequestBody = new PaymentRequestBody(paymentTypeId: paymentTypeId,
                    amount: amount, dateOfPayment: date, description: description)
        when: "REST payment on account url is hit"
            def response = sendPut(controller, "/accounts/$accountId/payment", paymentRequestBody)
        then:
            responseStatusCodeIsNoContent(response)
            responseContentTypeIsJson(response)
    }

    @Unroll
    def "should return status #badRequestStatus when hits URL for payments on account with invalid parameters"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountUtil.getDefaultAccountPersisted()
            String accountId = defaultAccount.getId()
        and: "data of payment"
            PaymentRequestBody paymentRequestBody = new PaymentRequestBody(paymentTypeId: paymentTypeId, amount: amount, dateOfPayment: date)
        when: "REST payment on account url is hit"
            def response = sendPut(controller, "/accounts/$accountId/payment", paymentRequestBody)
        then:
            response.status == badRequestStatus
        where:
        amount | paymentTypeId | date  | badRequestStatus
        null   | "1"           | TODAY | HttpStatus.BAD_REQUEST.value()
        30000  | "1"           | null  | HttpStatus.BAD_REQUEST.value()
        30000  | null          | TODAY | HttpStatus.BAD_REQUEST.value()
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

    def sendPost(controller, path, body){
        MockMvc mockMvc = standaloneSetup(controller)
                .build()
        def response = mockMvc.perform(
                post(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body))
                )
                .andDo(log())
                .andReturn().response

        return response
    }

    def sendPut(controller, path, body){
        MockMvc mockMvc = standaloneSetup(controller)
                .build()
        def response = mockMvc.perform(
                put(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body))
        )
                .andDo(log())
                .andReturn().response

        return response
    }

    def toJson(Object object) {
        return JsonOutput.toJson(object)
    }

    def Account getDefaultAccount() {
        AccountUtil.getDefaultAccountPersisted()
    }

    def deposit(Account account, Deposit deposit) {
        account.deposit(deposit)
    }

    def payment(Account account, BigDecimal amount, Date date){
        account.charge(new Payment(amount: amount, date: date))
    }

    def responseStatusCodeIsOk(MockHttpServletResponse response) {
        response.status == HttpStatus.OK.value()
    }

    def responseContentTypeIsJson(MockHttpServletResponse response) {
        response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def notExistDepositsInAccount(MockHttpServletResponse response) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        jsonResponse.deposits.size == 0
        jsonResponse.total == ZERO
    }

    def responseStatusCodeIsNoContent(MockHttpServletResponse response) {
        response.status == HttpStatus.NO_CONTENT.value()
    }
}
