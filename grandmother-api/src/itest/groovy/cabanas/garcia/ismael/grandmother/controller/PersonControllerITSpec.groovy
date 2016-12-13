package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.domain.account.ChargeType
import cabanas.garcia.ismael.grandmother.domain.person.Person
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

/**
 * Created by XI317311 on 12/12/2016.
 */
class PersonControllerITSpec extends RestIntegrationBaseSpec {

    @Override
    String getBasePath() {
        return "persons/"
    }

    @Unroll
    def "should return #statusCodeExpected status code when create a person with name '#name'"(){
        given:
            Person person = new Person(name: name)
            RequestEntity<Person> requestEntity = RequestEntity.post(serviceURI()).body(person)
        when:
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class)
        then:
            response.statusCode == statusCodeExpected
        where:
        name   | statusCodeExpected
        ""     | HttpStatus.BAD_REQUEST
        null   | HttpStatus.BAD_REQUEST
        "Agua" | HttpStatus.CREATED
    }
}
