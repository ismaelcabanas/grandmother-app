package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by XI317311 on 09/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChargeTypeControllerITSpec extends Specification{

    @Value('${local.server.port}')
    int port

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate()

    def "should return 201 status code when create a charge type"(){
        given:
            ChargeType chargeType = new ChargeType(name: "Agua")
            RequestEntity<ChargeType> requestEntity = RequestEntity.post(serviceURI("chargeTypes")).body(chargeType)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == HttpStatus.CREATED
    }

    def "should return 400 status code when create a charge type without required params"(){
        given:
            ChargeType chargeType = new ChargeType(name: chargeTypeName)
            RequestEntity<ChargeType> requestEntity = RequestEntity.post(serviceURI("chargeTypes")).body(chargeType)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        chargeTypeName | statusCodeExpected
        ""             | HttpStatus.BAD_REQUEST
        null           | HttpStatus.BAD_REQUEST
    }

    URI serviceURI(String path = "") {
        new URI("http://localhost:$port/${path}")
    }
}
