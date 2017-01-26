package cabanas.garcia.ismael.grandmother.util

import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.domain.person.repository.PersonRepository
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil

/**
 * Created by XI317311 on 25/01/2017.
 */
final class PersonITUtil {

    static Person createDefault(PersonRepository personRepository){
        personRepository.save(new Person(name: PersonUtil.DEFAULT_PERSON_NAME))
    }
}
