package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Deposit
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import cabanas.garcia.ismael.grandmother.utils.AccountTestUtils
import cabanas.garcia.ismael.grandmother.utils.DateUtilTest
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import cabanas.garcia.ismael.grandmother.utils.PaymentTypeTestUtil
import cabanas.garcia.ismael.grandmother.utils.PersonUtilTest
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.*
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.TWENTY_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.deposit
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.deposit
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.deposit
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getDefaultAccount
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getTEN_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getTHIRTY_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getTWENTY_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getTWENTY_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.TODAY
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.getTODAY
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.getTODAY
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.getYESTERDAY
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.getYESTERDAY
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.oneYearBeforeFrom
import static cabanas.garcia.ismael.grandmother.utils.DateUtilTest.oneYearBeforeFrom
import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeTestUtil.*
import static cabanas.garcia.ismael.grandmother.utils.RestUtilsTest.sendGet

/**
 * Created by XI317311 on 04/01/2017.
 */
class PaymentsAccountControllerSpec extends Specification{

    @Shared
    MockHttpServletResponse response

    def "should return paymens response ordered ascending by date when hits URL for getting payments on an account for a given year and month" (){
        given: "an account"
            Account account = getDefaultAccount()
        and: "some payments on account in diferent dates"
            payment(account, paymentAguaOf10000FronJune2010())
            payment(account, paymentAguaOf10000Fron1August2011())
            payment(account, paymentAguaOf10000Fron8August2011())
        and: "account controller configured with his services"
            AccountService accountService = Mock(AccountService)
            PaymentsAccountController controller = new PaymentsAccountController()
        when: "REST payments on account url is hit"
            def response = sendGet(controller, "/accounts/$account.id/payments?year=$year&month=$month")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
        and:
            1 * accountService.getPaymentTransactionsByYearAndMonth(_,_,_)
        and:
            responseSizeOfPaymentsIs(result.size())
            totalPaymensTransactionsAre(THIRTY_THOUSAND)
            paymentTransactionsReturnedAre(result)
        where:
        year | month | result
        2010 | 1     | []
        2001 | 8     | []
        2011 | 8     | [paymentAguaOf10000Fron1August2011(), paymentAguaOf10000Fron8August2011()]

    }

    def void paymentTransactionsReturnedAre(List<Payment> payments) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        payments.eachWithIndex { Payment payment, int i ->
            assert jsonResponse.payments[i].amount == payment.amount
            assert jsonResponse.payments[i].date == payment.date
            assert jsonResponse.payments[i].type.name == payment.type.name
        }
    }

    def void totalPaymensTransactionsAre(BigDecimal amount) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.total == amount
    }

    def void responseSizeOfPaymentsIs(int size) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.payments.size == size
    }

    Payment paymentAguaOf10000FronJune2010(){
        Date june2010 = DateUtils.parse("2010-06-01 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: june2010, type: getAguaPayment(), description: "")
    }

    Payment paymentAguaOf10000Fron1August2011(){
        Date august2011Day1 = DateUtils.parse("2011-08-01 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: august2011Day1, type: getAguaPayment(), description: "")
    }

    Payment paymentAguaOf10000Fron8August2011(){
        Date august2011Day10 = DateUtils.parse("2011-08-10 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: august2011Day10, type: getAguaPayment(), description: "")
    }

}
