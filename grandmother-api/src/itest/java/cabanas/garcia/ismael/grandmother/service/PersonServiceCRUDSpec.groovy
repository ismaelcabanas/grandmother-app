package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.model.Person
import cabanas.garcia.ismael.grandmother.model.impl.PersonImpl
import cabanas.garcia.ismael.grandmother.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.service.impl.PersonServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by XI317311 on 05/12/2016.
 */
@SpringBootTest
@Stepwise
class PersonServiceCRUDSpec extends Specification{

    public static final String ISMAEL_NAME = "Ismael"

    @Autowired
    PersonRepository personRepository

    def "remove all persons"(){
        given: "person repository service"
            PersonService personService = new PersonServiceImpl(repository: personRepository)
        when: "delete all persons"
            personService.deleteAll()
        then:
            List<Person> allPersons = personService.findAll()
            allPersons.isEmpty()
    }
    /*
    def "create persons"(){
        given:
            Person ismael = new PersonImpl(name: ISMAEL_NAME)
            PersonService personService = new PersonServiceImpl()
        when:
            personService.create(ismael)
        then:
            List<Person> allPersons = personService.findAll()
            allPersons != null
            allPersons.size() == 1
            with(allPersons.get(0)){
                getName() == ISMAEL_NAME
            }
    }
    */
}
