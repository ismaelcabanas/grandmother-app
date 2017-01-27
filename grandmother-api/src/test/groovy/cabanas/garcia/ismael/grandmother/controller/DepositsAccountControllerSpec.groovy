package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.DepositAccountService
import cabanas.garcia.ismael.grandmother.stubs.service.DepositAccountServiceWithDepositsInAccountStub
import cabanas.garcia.ismael.grandmother.utils.test.DepositUtil
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil
import groovy.json.JsonSlurper
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.DateUtils.format
import static cabanas.garcia.ismael.grandmother.utils.test.AccountUtil.getDefaultAccountPersisted
import static cabanas.garcia.ismael.grandmother.utils.test.AmountUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.DateUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.PersonUtil.getDefaultPersonPersisted
import static cabanas.garcia.ismael.grandmother.utils.test.RestUtil.*

/**
 * Created by XI317311 on 30/12/2016.
 */
class DepositsAccountControllerSpec extends Specification{

    @Shared
    MockHttpServletResponse response

    def "should return deposits response when hits URL for getting deposits on an account without deposits" (){
        given: "an account without deposits"
            Account defaultAccount = getDefaultAccountPersisted()
        and: "prepare SUT"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: defaultAccount)
            DepositsAccountController classUnderTest = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            MockHttpServletResponse response = sendGet(classUnderTest, "/accounts/$defaultAccount.id/deposits")
        then:
            responseStatusCodeIs200(response)
        and:
            responseContentTypeIsJson(response)
        and:
            responseNotContainsDeposits(response)

    }

    def "should return deposit transactions with total amount when hits the URL for getting deposits for an account"(){
        given: "an account"
            Account defaultAccount = getDefaultAccountPersisted()
        and: "a given person"
            Person defaultPerson = getDefaultPersonPersisted()
        and: "that person did a deposit of 10000 today on account"
            Deposit depositFromDefaultPersonOf10000AtToday =
                    Deposit.builder()
                            .amount(TEN_THOUSAND)
                            .date(TODAY)
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(defaultPerson)
                            .build()
            defaultAccount.deposit(depositFromDefaultPersonOf10000AtToday)
        and: "that person did a deposit of 20000 yesterday on account"
            Deposit depositFromDefaultPersonOf20000Yesterday =
                    Deposit.builder()
                            .amount(TWENTY_THOUSAND)
                            .date(YESTERDAY)
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(defaultPerson)
                            .build()
            defaultAccount.deposit(depositFromDefaultPersonOf20000Yesterday)
        and: "prepare SUT"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: defaultAccount)
            DepositsAccountController classUnderTest = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
           MockHttpServletResponse response = sendGet(classUnderTest, "/accounts/$defaultAccount.id/deposits")
        then:
            responseStatusCodeIs200(response)
        and:
            responseContentTypeIsJson(response)
        and:
            totalAmountDepositsExpected(response, THIRTY_THOUSAND)
        and:
            responseContainsDeposits(response,
                    depositFromDefaultPersonOf20000Yesterday,
                    depositFromDefaultPersonOf10000AtToday)
    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given person" (){
        given: "an account"
            Account defaultAccount = getDefaultAccountPersisted()
        and: "person ismael did today a deposit of 10000 on account"
            Person ismael = PersonUtil.getDefaultPersonPersisted()
            Deposit depositFromIsmaelOf10000AtToday =
                Deposit.builder()
                        .amount(TEN_THOUSAND)
                        .date(TODAY)
                        .description(DepositUtil.DEFAULT_DESCRIPTION)
                        .person(ismael)
                        .build()
            defaultAccount.deposit(depositFromIsmaelOf10000AtToday)
        and: "person ismael did yesterday a deposit of 20000 on account"
            Deposit depositFromIsmaelOf20000Yesterday =
                    Deposit.builder()
                            .amount(TWENTY_THOUSAND)
                            .date(YESTERDAY)
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(ismael)
                            .build()
            defaultAccount.deposit(depositFromIsmaelOf20000Yesterday)
        and: "person bea did today a deposit of 20000 on account"
            Person bea = PersonUtil.getPersistedBea()
            Deposit depositFromBeaOf20000Today =
                Deposit.builder()
                        .amount(TWENTY_THOUSAND)
                        .date(TODAY)
                        .description(DepositUtil.DEFAULT_DESCRIPTION)
                        .person(bea)
                        .build()
            defaultAccount.deposit(depositFromBeaOf20000Today)
        and: "prepare SUT"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: defaultAccount)
            DepositsAccountController classUnderTest = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            MockHttpServletResponse response = sendGet(classUnderTest, "/accounts/$defaultAccount.id/deposits?person_id=$ismael.id")
        then:
            responseStatusCodeIs200(response)
        and:
            responseContentTypeIsJson(response)
        and:
            totalAmountDepositsExpected(response, THIRTY_THOUSAND)
        and:
            responseContainsDeposits(response,
                    depositFromIsmaelOf20000Yesterday,
                    depositFromIsmaelOf10000AtToday)

    }

    def "should return deposits response ordered ascending by date when hits URL for getting deposits on an account with deposits for a given year and person" (){
        given: "an account"
            Account defaultAccount = getDefaultAccountPersisted()
        and: "person ismael did today a deposit of 10000 on account"
            Person ismael = PersonUtil.getDefaultPersonPersisted()
            Deposit depositFromIsmaelOf10000ThisYear =
                    Deposit.builder()
                            .amount(TEN_THOUSAND)
                            .date(TODAY)
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(ismael)
                            .build()
            defaultAccount.deposit(depositFromIsmaelOf10000ThisYear)
        and: "person ismael did yesterday a deposit of 20000 on account"
            Deposit depositFromIsmaelOf20000LastYear =
                    Deposit.builder()
                            .amount(TWENTY_THOUSAND)
                            .date(oneYearBeforeFrom(TODAY))
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(ismael)
                            .build()
            defaultAccount.deposit(depositFromIsmaelOf20000LastYear)
        and: "person bea did today a deposit of 20000 on account"
            Person bea = PersonUtil.getPersistedBea()
            Deposit depositFromBeaOf20000LastYear =
                    Deposit.builder()
                            .amount(TWENTY_THOUSAND)
                            .date(oneYearBeforeFrom(TODAY))
                            .description(DepositUtil.DEFAULT_DESCRIPTION)
                            .person(bea)
                            .build()
            defaultAccount.deposit(depositFromBeaOf20000LastYear)
        and: "prepare SUT"
            DepositAccountService depositAccountService = new DepositAccountServiceWithDepositsInAccountStub(account: defaultAccount)
            DepositsAccountController classUnderTest = new DepositsAccountController(depositAccountService: depositAccountService)
        when: "REST deposits on account url is hit"
            int year = yearOf(depositFromIsmaelOf10000ThisYear.date)
            MockHttpServletResponse response = sendGet(classUnderTest, "/accounts/$defaultAccount.id/deposits?person_id=$ismael.id&year=$year")
        then:
            responseStatusCodeIs200(response)
        and:
            responseContentTypeIsJson(response)
        and:
            totalAmountDepositsExpected(response, TEN_THOUSAND)
        and:
            responseContainsDeposits(response,
                    depositFromIsmaelOf10000ThisYear)
    }

    void responseContainsDeposits(MockHttpServletResponse mockHttpServletResponse, Deposit... deposits) {
        def jsonResponse = new JsonSlurper().parseText(mockHttpServletResponse.contentAsString)
        jsonResponse.deposits.eachWithIndex{ def depositResponse, int i ->
            assert depositResponse.amount == deposits[i].amount
            assert depositResponse.description == deposits[i].description
            assert depositResponse.date == format(deposits[i].date)
            assert depositResponse.person.id == deposits[i].person.id
            assert depositResponse.person.name == deposits[i].person.name
        }
    }

    void totalAmountDepositsExpected(MockHttpServletResponse mockHttpServletResponse, BigDecimal expectedAmount) {
        def jsonResponse = new JsonSlurper().parseText(mockHttpServletResponse.contentAsString)
        assert jsonResponse.total == expectedAmount
    }

    void responseNotContainsDeposits(MockHttpServletResponse mockHttpServletResponse) {
        def jsonResponse = new JsonSlurper().parseText(mockHttpServletResponse.contentAsString)
        assert jsonResponse.deposits.size == 0
    }

}
