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

import static cabanas.garcia.ismael.grandmother.utils.test.PaymentTypeUtil.*
import static cabanas.garcia.ismael.grandmother.utils.test.RestUtil.sendGet

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

    def "when hits URL for getting payments types, the payment type service should get all payment types"(){
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
            paymentTypeList.add(getGasPersistedPayment())
            paymentTypeList.add(getEndesaPersistedPayment())
            paymentTypeList.add(getAguaPersistedPayment())
        and:
            PaymentTypeService paymentTypeService = new AllPaymentTypeService(paymentTypeList)
            PaymentTypeController controller = new PaymentTypeController(paymentTypeService: paymentTypeService)
        when:
            response = sendGet(controller, "/paymentTypes")
        then:
            responseContentIsNotEmpty()
            responseSizeOfPaymentsTypeIs(3)
            responsePaymentsTypeAre(getAguaPersistedPayment(), getEndesaPersistedPayment(), getGasPersistedPayment())
    }

    def "should return payment type when hits URL for getting a payment type determinded"(){
        given:
            PaymentType gasPaymentType = getGasPersistedPayment()
            List<PaymentType> paymentTypeList = new ArrayList<>()
            paymentTypeList.add(getGasPersistedPayment())
            paymentTypeList.add(getEndesaPersistedPayment())
        and:
            PaymentTypeService paymentTypeService = new AllPaymentTypeService(paymentTypeList)
            PaymentTypeController controller = new PaymentTypeController(paymentTypeService: paymentTypeService)
        when:
            response = sendGet(controller, "/paymentTypes/${gasPaymentType.id}")
        then:
            response.status == HttpStatus.OK.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
            responsePaymentTypeIs(gasPaymentType)
    }

    def void responsePaymentTypeIs(PaymentType paymentType) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.id == paymentType.id
        assert jsonResponse.name == paymentType.name
    }

    def void responsePaymentsTypeAre(PaymentType... paymentTypes) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        paymentTypes.eachWithIndex { PaymentType paymentType, int i ->
            assert jsonResponse.paymentTypes[i].name == paymentType.name
            assert jsonResponse.paymentTypes[i].id != null
        }
    }

    def void responseSizeOfPaymentsTypeIs(int size) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        assert jsonResponse.paymentTypes.size == size
    }

    def void responseContentIsNotEmpty() {
        String content = response.contentAsString
        assert content != null
        assert !content.isEmpty()
    }
}
