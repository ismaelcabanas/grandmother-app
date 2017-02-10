package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.TestConfiguration
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.impl.RepositoryPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
@ContextConfiguration(classes = TestConfiguration.class) // not mentioned by docs, but had to include this for Spock to startup the Spring context
@DataJpaTest
//@Stepwise
class PersonServiceCRUDITSpec extends Specification{

    public static final String ISMAEL_NAME = "Ismael"

    @Autowired
    PersonRepository personRepository

    def "remove all persons"(){
        given: "person service"
            PersonService personService = new RepositoryPersonService(personRepository: personRepository)
        when: "delete all persons"
            personService.deleteAll()
        then:
            List<Person> allPersons = personService.findAll()
            allPersons.isEmpty()
    }

    def "create persons"(){
        given: "a person data"
            Person ismael = new Person(name: ISMAEL_NAME)
        and: "person service"
            PersonService personService = new RepositoryPersonService(personRepository: personRepository)
        when:
            personService.create(ismael)
        then:
            List<Person> allPersons = personService.findAll()
            allPersons != null
        and:
            allPersons.size() == 1
        and:
            with(allPersons.get(0)){
                getName() == ISMAEL_NAME
            }
    }

    def "find one person" (){
        given: "a new person"
            Person ismael = new Person(name: ISMAEL_NAME)
            PersonService personService = new RepositoryPersonService(personRepository: personRepository)
            personService.create(ismael)
        when: "find person by identifier"
            Person person = personService.findById(ismael.getId())
        then:
            with(person){
                getName() == ISMAEL_NAME
            }
    }

    def "delete a person"(){
        given: "a new person"
            Person ismael = new Person(name: ISMAEL_NAME)
            PersonService personService = new RepositoryPersonService(personRepository: personRepository)
            personService.create(ismael)
        when: "delete the person"
            personService.delete(ismael)
        then:
            Person person = personService.findById(ismael.getId())
        and:
            person == null
    }

    def "update a person" (){
        given: "a new person"
            Person ismael = new Person(name: ISMAEL_NAME)
            PersonService personService = new RepositoryPersonService(personRepository: personRepository)
            personService.create(ismael)
        and: "update his name"
            ismael.changeName("Bea")
        when: "update "
            Person updatedPerson = personService.update(ismael)
        then:
            with(updatedPerson){
                getName() == "Bea"
            }
    }

}
