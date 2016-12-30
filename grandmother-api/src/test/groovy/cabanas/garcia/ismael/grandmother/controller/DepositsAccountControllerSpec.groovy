package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.*
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.*
import static cabanas.garcia.ismael.grandmother.utils.RestUtilsTest.*

/**
 * Created by XI317311 on 30/12/2016.
 */
class DepositsAccountControllerSpec extends Specification{

    def "should return deposits response when hits URL for getting deposits on an account without deposits" (){
        given: "an account without deposits"
            Account account = getDefaultAccount()
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account)
            DepositsAccountController controller = new DepositsAccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            notExistDepositsInAccount(response)
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits" (){
        given: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Person ismael = PersonUtilTest.getIsmael()
            Deposit deposit10000 = new Deposit(amount: TEN_THOUSAND, date: TODAY, person: ismael)
            Deposit deposit20000 = new Deposit(amount: TWENTY_THOUSAND, date: YESTERDAY, person: ismael)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        and: "account controller configured with his services"
            AccountService accountService = new AccountServiceThatGetAnAccountStub(account)
            DepositsAccountController controller = new DepositsAccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            totalDepositsAmount(response) == THIRTY_THOUSAND
            sizeOfDepositTransactions(response) == 2
            depositTransactionReturnedAre(response, deposit10000, deposit20000) == true
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given person" (){
        given: "ismael and bea persons"
            Person ismael = PersonUtilTest.getIsmael()
            Person bea = PersonUtilTest.getBea()
        and: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Deposit deposit10000 = new Deposit(amount: TEN_THOUSAND, date: TODAY, person: ismael)
            Deposit deposit20000 = new Deposit(amount: TWENTY_THOUSAND, date: YESTERDAY, person: bea)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        and: "account controller configured with his services"
            AccountService accountService = Spy(AccountServiceThatGetAnAccountStub, constructorArgs: [account])
            DepositsAccountController controller = new DepositsAccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/deposits?person_id=$ismael.id")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            1 * accountService.getDepositTransactionsByPersonId(_,_)
        and:
            totalDepositsAmount(response) == TEN_THOUSAND
            sizeOfDepositTransactions(response) == 1
            depositTransactionReturnedAre(response, deposit10000) == true
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given year" (){
        given: "ismael and bea persons"
            Person ismael = PersonUtilTest.getIsmael()
            Person bea = PersonUtilTest.getBea()
        and: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Deposit deposit10000 = new Deposit(amount: TEN_THOUSAND, date: lastYearFrom(TODAY), person: ismael)
            Deposit deposit30000 = new Deposit(amount: THIRTY_THOUSAND, date: lastYearFrom(TODAY), person: ismael)
            Deposit deposit20000 = new Deposit(amount: TWENTY_THOUSAND, date: YESTERDAY, person: bea)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
            deposit(account, deposit30000)
        and: "account controller configured with his services"
            AccountService accountService = Spy(AccountServiceThatGetAnAccountStub, constructorArgs: [account])
            DepositsAccountController controller = new DepositsAccountController(accountService: accountService)
        when: "REST deposits on account url is hit"
            int year = DateUtilTest.yearOf(YESTERDAY)
            def response = sendGet(controller, "/accounts/$account.id/deposits?year=$year")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            1 * accountService.getDepositTransactionsByYear(_,_)
        and:
            totalDepositsAmount(response) == TWENTY_THOUSAND
            sizeOfDepositTransactions(response) == 1
            depositTransactionReturnedAre(response, deposit20000) == true
    }

    boolean depositTransactionReturnedAre(MockHttpServletResponse response, Deposit... deposits) {
        boolean flag = true
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        for(int i=0; i < deposits.length; i++){
            if((jsonResponse.deposits[i].amount != deposits[i].amount) ||
                    (jsonResponse.deposits[i].date != DateUtils.format(deposits[i].date))){
                flag = false
                break
            }
        }
        return flag
    }

    int sizeOfDepositTransactions(MockHttpServletResponse response) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        return jsonResponse.deposits.size
    }

    BigDecimal totalDepositsAmount(MockHttpServletResponse response) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        return jsonResponse.total
    }

}
