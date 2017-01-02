package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import cabanas.garcia.ismael.grandmother.service.PaymentTypeService
import cabanas.garcia.ismael.grandmother.stubs.service.paymenttype.AllPaymentTypeService
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import static cabanas.garcia.ismael.grandmother.utils.PaymentTypeTestUtil.*
import static cabanas.garcia.ismael.grandmother.utils.RestUtilsTest.sendGet

/**
 * Created by XI317311 on 02/01/2017.
 */
class PaymentControllerSpec extends Specification {

    @Shared
    MockHttpServletResponse response

    def "should return 200 status code when hits URL for getting payments types"(){
        given:
            PaymentTypeController controller = new PaymentTypeController()
        when:
            response = sendGet(controller, "/paymentTypes")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def "when hits URL for getting payments types, payment type service should get all payment types"(){
        given:
            PaymentTypeService paymentTypeService = Mock(PaymentTypeService.class)
            PaymentTypeController controller = new PaymentTypeController(paymentTypeService: paymentTypeService)
        when:
            response = sendGet(controller, "/paymentTypes")
        then:
            1 * paymentTypeService.findAll()
    }

    def "should return payment type ordered by payment name list when hits URL for getting payments types"(){
        given:
            List<PaymentType> paymentTypeList = new ArrayList<>()
            paymentTypeList.add(getGasPayment())
            paymentTypeList.add(getEndesaPayment())
            paymentTypeList.add(getAguaPayment())
        and:
            PaymentTypeService paymentTypeService = new AllPaymentTypeService(paymentTypeList)
            PaymentTypeController controller = new PaymentTypeController(paymentTypeService: paymentTypeService)
        when:
            response = sendGet(controller, "/paymentTypes")
        then:
            responseContentIsNotEmpty()
            responseSizeOfPaymentsTypeIs(3)
            responsePaymentsTypeAre(getAguaPayment(), getEndesaPayment(), getGasPayment())
    }

    def responsePaymentsTypeAre(PaymentType... paymentTypes) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        paymentTypes.eachWithIndex { PaymentType paymentType, int i ->
            assert jsonResponse.paymentTypes[i] == paymentType.name
        }
    }

    def responseSizeOfPaymentsTypeIs(int size) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.paymentTypes.size == size
    }

    def void responseContentIsNotEmpty() {
        String content = response.contentAsString
        assert content != null
        assert !content.isEmpty()
    }
}
