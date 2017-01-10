package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.Account
import cabanas.garcia.ismael.grandmother.domain.account.Payment
import cabanas.garcia.ismael.grandmother.service.AccountService
import cabanas.garcia.ismael.grandmother.service.PaymentAccountService
import cabanas.garcia.ismael.grandmother.stubs.service.AccountServiceThatGetAnAccountStub
import cabanas.garcia.ismael.grandmother.stubs.service.PaymentAccountServiceWithPaymentsInAccountStub
import cabanas.garcia.ismael.grandmother.utils.DateUtils
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.*
import static cabanas.garcia.ismael.grandmother.utils.AccountTestUtils.getTEN_THOUSAND
import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeTestUtil.*
import static cabanas.garcia.ismael.grandmother.utils.RestUtilsTest.sendGet

/**
 * Created by XI317311 on 04/01/2017.
 */
class PaymentsAccountControllerSpec extends Specification{

    @Shared
    MockHttpServletResponse response

    @Unroll
    def "should return paymens response ordered ascending by date when hits URL for getting payments on an account for a given year #year and month #month" (){
        given: "an account"
            Account account = getDefaultAccount()
        and: "some payments on account in diferent dates"
            payment(account, paymentAguaOf10000FromJune2010())
            payment(account, paymentAguaOf10000From1August2011())
            payment(account, paymentAguaOf10000From8August2011())
        and: "account controller configured with his services"
            PaymentAccountService paymentAccountService =
                    new PaymentAccountServiceWithPaymentsInAccountStub(account)
            PaymentsAccountController controller = new PaymentsAccountController(paymentAccountService: paymentAccountService)
        when: "REST payments on account url is hit"
            response = sendGet(controller, "/accounts/$account.id/payments?year=$year&month=$month")
        then:
            responseIsOk()
        and:
            responseSizeOfPaymentsIs(payments.size())
            totalPaymensTransactionsAre(totalAmount)
            paymentTransactionsReturnedAre(payments)
        where:
        year | month | payments                                                                   | totalAmount
        2010 | 1     | []                                                                         | 0
        2001 | 8     | []                                                                         | 0
        2011 | 8     | [paymentAguaOf10000From8August2011(), paymentAguaOf10000From1August2011()] | 20000

    }

    def void responseIsOk() {
        response.status == HttpStatus.OK.value()
        response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def void paymentTransactionsReturnedAre(List<Payment> payments) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        payments.eachWithIndex { Payment payment, int i ->
            assert jsonResponse.payments[i].amount == payment.amount
            assert jsonResponse.payments[i].date == DateUtils.format(payment.date)
            assert jsonResponse.payments[i].paymentType.name == payment.type.name
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

    Payment paymentAguaOf10000FromJune2010(){
        Date june2010 = DateUtils.parse("2010-06-01 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: june2010, type: getAguaPayment(), description: "")
    }

    Payment paymentAguaOf10000From1August2011(){
        Date august2011Day1 = DateUtils.parse("2011-08-01 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: august2011Day1, type: getAguaPayment(), description: "")
    }

    Payment paymentAguaOf10000From8August2011(){
        Date august2011Day10 = DateUtils.parse("2011-08-08 00:00:00.0")
        new Payment(amount: TEN_THOUSAND, date: august2011Day10, type: getAguaPayment(), description: "")
    }

}
