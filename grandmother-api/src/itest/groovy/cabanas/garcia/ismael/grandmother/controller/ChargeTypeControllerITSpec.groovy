package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

/**
 * Created by XI317311 on 09/12/2016.
 */
class ChargeTypeControllerITSpec extends RestIntegrationBaseSpec{

    @Unroll
    def "should return #statusCodeExpected status code when create a charge type with name '#chargeTypeName'"(){
        given:
            ChargeType chargeType = new ChargeType(name: chargeTypeName)
            RequestEntity<ChargeType> requestEntity = RequestEntity.post(serviceURI("/chargeTypes")).body(chargeType)
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
