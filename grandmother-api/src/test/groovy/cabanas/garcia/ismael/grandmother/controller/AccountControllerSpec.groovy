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
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * Created by XI317311 on 15/12/2016.
 */
class AccountControllerSpec extends Specification{

    private static final BigDecimal ZERO = BigDecimal.ZERO
    private static final BigDecimal TEN_THOUSAND = new BigDecimal(10000)
    private static final BigDecimal TWENTY_THOUSAND = new BigDecimal(20000)
    private static final BigDecimal THIRTY_THOUSAND = new BigDecimal(30000)

    def "should return status 200 when hits the URL for getting an existing account"(){
        given: "a given account identifier"
            Account account = getDefaultAccount()
        and: "account controller configured with stub service"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST account get url is hit"
            def response = sendGet(controller, "/accounts/$account.id")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def "should get account details when hits the URL for getting an existing account"(){
        given: "a given account with list"
            Account account = getDefaultAccount()
            deposit(account, TEN_THOUSAND, TODAY)
            deposit(account, 15000, YESTERDAY)
            payment(account, TEN_THOUSAND, TODAY)
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

    def "should get account details when hits the URL for getting an existing account with balance 0"(){
        given: "a given account with balance 0"
            Account account = Account.builder().id("1").accountNumber("123").balance(0).build()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
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
                .accountNumber(AccountTestUtils.DEFAULT_ACCOUNT_NUMBER)
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
                .balance(AccountTestUtils.DEFAULT_BALANCE)
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
            Account defaultAccount = AccountTestUtils.getDefaultAccount()
            String accountId = defaultAccount.getId()
        and: "request body of deposit"
            DepositRequestBody depositRequestBody = DepositRequestBody.builder()
                    .personId("1")
                    .deposit(defaultAccount.balance)
                    .dateOfDeposit(TODAY)
                    .description("Transferencia a su favor")
                    .build()
        when: "REST desposit on account url is hit"
            def response = sendPut(controller, "/accounts/$accountId/deposit", depositRequestBody)
        then:
            response.status == HttpStatus.NO_CONTENT.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    @Unroll
    def "should return status #badRequestStatus when hits URL for depositing on account with invalid parameters"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountTestUtils.getDefaultAccount()
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
            Account defaultAccount = AccountTestUtils.getDefaultAccount()
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
            response.status == HttpStatus.NO_CONTENT.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    @Unroll
    def "should return status #badRequestStatus when hits URL for payments on account with invalid parameters"(){
        given: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            AccountController controller = new AccountController(accountService: accountService)
        and: "a account identifier"
            Account defaultAccount = AccountTestUtils.getDefaultAccount()
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

    def "should return deposits response when hits URL for getting deposits on an account without deposits" (){
        given: "an account without deposits"
            Account account = getDefaultAccount()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            responseStatusCodeIsOk(response)
            responseContentTypeIsJson(response)
        and:
            notExistDepositsInAccount(response)
    }



    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits" (){
        given: "an account with unordered deposits"
            Account account = getDefaultAccount()
            deposit(account, TEN_THOUSAND, TODAY)
            deposit(account, TWENTY_THOUSAND, YESTERDAY)
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account: account)
            AccountController controller = new AccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
            jsonResponse.deposits.size == 2
            jsonResponse.deposits[0].amount == TEN_THOUSAND
            jsonResponse.deposits[0].date == DateUtils.format(TODAY)
            jsonResponse.deposits[1].amount == TWENTY_THOUSAND
            jsonResponse.deposits[1].date == DateUtils.format(YESTERDAY)
            jsonResponse.total == THIRTY_THOUSAND
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
        AccountTestUtils.getDefaultAccount()
    }

    def deposit(Account account, BigDecimal amount, Date date) {
        account.deposit(new Deposit(amount: amount, date: date))
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
}
