package cabanas.garcia.ismael.grandmother.service

import cabanas.garcia.ismael.grandmother.model.Person
import cabanas.garcia.ismael.grandmother.model.impl.PersonImpl
import cabanas.garcia.ismael.grandmother.service.impl.PersonServiceImpl
import spock.lang.Specification

/**
 * Created by XI317311 on 05/12/2016.
 */
class PersonServiceCRUDSpec extends Specification{

    public static final String ISMAEL_NAME = "Ismael"

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
}
