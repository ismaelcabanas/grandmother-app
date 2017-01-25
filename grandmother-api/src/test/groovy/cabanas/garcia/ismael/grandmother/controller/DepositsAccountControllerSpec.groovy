package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.DepositAccountService
import cabanas.garcia.ismael.grandmother.stubs.service.DepositAccountServiceWithDepositsInAccountStub
import cabanas.garcia.ismael.grandmother.utils.test.AmountUtil

import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.test.AccountUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.RestUtil.sendGet

/**
 * Created by XI317311 on 30/12/2016.
 */
class DepositsAccountControllerSpec extends Specification{

    @Shared
    MockHttpServletResponse response

    def "should return deposits response when hits URL for getting deposits on an account without deposits" (){
        given: "an account without deposits"
            Account account = getDefaultAccount()
        and: "account controller configured with his services"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account:  account)
            DepositsAccountController controller = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            responseIsOk()
        and:
            sizeOfDepositTransactionsIs(0)
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits" (){
        given: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Person ismael = PersonUtil.getIsmael()
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: TODAY, person: ismael)
            Deposit deposit20000 = new Deposit(amount: AmountUtil.TWENTY_THOUSAND, date: YESTERDAY, person: ismael)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        and: "account controller configured with his services"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: account)
            DepositsAccountController controller = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            response = sendGet(controller, "/accounts/$account.id/deposits")
        then:
            responseIsOk()
        and:
            sizeOfDepositTransactionsIs(2)
            totalDepositsAmount(AmountUtil.THIRTY_THOUSAND)
            depositTransactionReturnedAre(deposit10000, deposit20000)
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given person" (){
        given: "ismael and bea persons"
            Person ismael = PersonUtil.getIsmael()
            Person bea = PersonUtil.getBea()
        and: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: TODAY, person: ismael)
            Deposit deposit20000 = new Deposit(amount: AmountUtil.TWENTY_THOUSAND, date: YESTERDAY, person: bea)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
        and: "account controller configured with his services"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: account)
            DepositsAccountController controller = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            response = sendGet(controller, "/accounts/$account.id/deposits?person_id=$ismael.id")
        then:
            responseIsOk()
        and:
            sizeOfDepositTransactionsIs(1)
            totalDepositsAmount(AmountUtil.TEN_THOUSAND)
            depositTransactionReturnedAre(deposit10000)
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given year and person" (){
        given: "ismael and bea persons"
            Person ismael = PersonUtil.getIsmael()
            Person bea = PersonUtil.getBea()
        and: "an account with unordered deposits"
            Account account = getDefaultAccount()
            Deposit deposit10000 = new Deposit(amount: AmountUtil.TEN_THOUSAND, date: oneYearBeforeFrom(TODAY), person: ismael)
            Deposit deposit30000 = new Deposit(amount: AmountUtil.THIRTY_THOUSAND, date: oneYearBeforeFrom(TODAY), person: ismael)
            Deposit deposit20000 = new Deposit(amount: AmountUtil.TWENTY_THOUSAND, date: YESTERDAY, person: bea)
            deposit(account, deposit10000)
            deposit(account, deposit20000)
            deposit(account, deposit30000)
        and: "account controller configured with his services"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: account)
            DepositsAccountController controller = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            int year = yearOf(YESTERDAY)
            response = sendGet(controller, "/accounts/$account.id/deposits?person_id=$bea.id&year=$year")
        then:
            responseIsOk()
        and:
            sizeOfDepositTransactionsIs(1)
            totalDepositsAmount(AmountUtil.TWENTY_THOUSAND)
            depositTransactionReturnedAre(deposit20000)
    }

    def void sizeOfDepositTransactionsIs(int size) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.deposits.size == size
    }

    def void responseIsOk() {
        assert response.status == HttpStatus.OK.value()
        assert response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
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

    def void totalDepositsAmount(BigDecimal amount) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.total == amount
    }

    def void depositTransactionReturnedAre(Deposit... deposits) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        deposits.eachWithIndex { Deposit deposit, int i ->
            assert jsonResponse.deposits[i].amount == deposit.amount
            assert jsonResponse.deposits[i].date == DateUtils.format(deposit.date)
        }
    }

}
