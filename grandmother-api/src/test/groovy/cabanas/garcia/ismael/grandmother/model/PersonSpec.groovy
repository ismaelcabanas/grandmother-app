package cabanas.garcia.ismael.grandmother.model

import cabanas.garcia.ismael.grandmother.model.impl.PersonImpl
import cabanas.garcia.ismael.grandmother.service.PersonService
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class PersonSpec extends Specification{

    def "should create new persons"(){
        given:
        PersonService mockPersonService = Mock(PersonService)
        Person person = new PersonImpl(name: "Ismael", personService: mockPersonService)

        when:
        person.create()

        then:
        1 * mockPersonService.create(person)
    }
}
