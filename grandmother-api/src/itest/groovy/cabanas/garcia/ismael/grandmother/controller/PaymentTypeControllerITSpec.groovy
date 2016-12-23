package cabanas.garcia.ismael.grandmother.controller

import static cabanas.garcia.ismael.grandmother.controller.PaymentTypeController.PAYMENT_TYPE_BASE_PATH

import cabanas.garcia.ismael.grandmother.domain.account.PaymentType
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

/**
 * Created by XI317311 on 09/12/2016.
 */
class PaymentTypeControllerITSpec extends RestIntegrationBaseSpec{

    @Unroll
    def "should return #statusCodeExpected status code when create a charge type with name '#chargeTypeName'"(){
        given:
            PaymentType chargeType = new PaymentType(name: chargeTypeName)
            RequestEntity<PaymentType> requestEntity = RequestEntity.post(serviceURI(PAYMENT_TYPE_BASE_PATH)).body(chargeType)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        chargeTypeName | statusCodeExpected
        ""             | HttpStatus.BAD_REQUEST
        null           | HttpStatus.BAD_REQUEST
        "Agua"         | HttpStatus.CREATED
    }

}
