package cabanas.garcia.ismael.grandmother.domain

import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.PersonService
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class PersonSpec extends Specification{

    def "should create new persons"(){
        given:
        PersonService mockPersonService = Mock(PersonService)
        Person person = new Person(name: "Ismael", personService: mockPersonService)

        when:
        person.create()

        then:
        1 * mockPersonService.create(person)
    }
}
